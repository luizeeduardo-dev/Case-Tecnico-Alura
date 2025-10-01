package br.com.alura.AluraFake.task.service;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.task.dto.MultipleChoiceTaskDTO;
import br.com.alura.AluraFake.task.dto.OpenTextTaskDTO;
import br.com.alura.AluraFake.task.dto.SingleChoiceTaskDTO;
import br.com.alura.AluraFake.task.dto.TaskOptionDTO;
import br.com.alura.AluraFake.task.entities.OpenTextTask;
import br.com.alura.AluraFake.task.entities.options.ChoiceTask;
import br.com.alura.AluraFake.task.entities.options.MultipleChoiceTask;
import br.com.alura.AluraFake.task.entities.options.TaskOption;
import br.com.alura.AluraFake.task.repository.TaskRepository;
import br.com.alura.AluraFake.task.entities.options.SingleChoiceTask;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

  private final TaskRepository taskRepository;
  private final CourseRepository courseRepository;

  public TaskService(TaskRepository taskRepository, CourseRepository courseRepository) {
    this.taskRepository = taskRepository;
    this.courseRepository = courseRepository;
  }

  @Transactional
  public void createOpenTextTask(OpenTextTaskDTO taskDTO) {
    Course course = findAndValidateCourse(taskDTO.courseId());
    validateTaskCreation(course.getId(), taskDTO.statement(), taskDTO.order());
    handleTaskReordering(taskDTO.courseId(), taskDTO.order());

    OpenTextTask task = new OpenTextTask();
    task.setStatement(taskDTO.statement());
    task.setOrder(taskDTO.order());
    task.setCourse(course);

    this.taskRepository.save(task);
  }

  @Transactional
  public void createSingleChoiceTask(SingleChoiceTaskDTO taskDTO) {
    Course course = findAndValidateCourse(taskDTO.courseId());
    validateTaskCreation(course.getId(), taskDTO.statement(), taskDTO.order());
    validateChoiceOptions(taskDTO.options(), taskDTO.statement());
    validateSingleChoiceRules(taskDTO.options());

    handleTaskReordering(course.getId(), taskDTO.order());

    SingleChoiceTask task = new SingleChoiceTask();
    task.setStatement(taskDTO.statement());
    task.setOrder(taskDTO.order());
    task.setCourse(course);
    mapAndSetOptions(task, taskDTO.options());

    this.taskRepository.save(task);
  }

  @Transactional
  public void createMultipleChoiceTask(MultipleChoiceTaskDTO taskDTO) {
    Course course = findAndValidateCourse(taskDTO.courseId());
    validateTaskCreation(course.getId(), taskDTO.statement(), taskDTO.order());
    validateChoiceOptions(taskDTO.options(), taskDTO.statement());
    validateMultipleChoiceRules(taskDTO.options());

    handleTaskReordering(course.getId(), taskDTO.order());

    MultipleChoiceTask task = new MultipleChoiceTask();
    task.setStatement(taskDTO.statement());
    task.setOrder(taskDTO.order());
    task.setCourse(course);

    mapAndSetOptions(task, taskDTO.options());

    this.taskRepository.save(task);
  }

  private void mapAndSetOptions(ChoiceTask taskEntity, List<TaskOptionDTO> optionDTOs) {
    List<TaskOption> optionEntities =
        optionDTOs.stream()
            .map(
                dto -> {
                  TaskOption optionEntity = new TaskOption();
                  optionEntity.setText(dto.text());
                  optionEntity.setIsCorrect(dto.isCorrect());
                  optionEntity.setTask(taskEntity);
                  return optionEntity;
                })
            .toList();

    taskEntity.setOptions(null);
    taskEntity.setOptions(optionEntities);
  }

  private Course findAndValidateCourse(Long courseId) {
    Course course =
        this.courseRepository
            .findById(courseId)
            .orElseThrow(() -> new ValidationException("Curso não encontrado."));

    if (course.getStatus() != Status.BUILDING) {
      throw new ValidationException(
          "Só é possível adicionar atividades em cursos com status BUILDING.");
    }
    return course;
  }

  private void validateTaskCreation(Long courseId, String statement, Integer newOrder) {

    if (this.taskRepository.existsByCourseIdAndStatement(courseId, statement)) {
      throw new ValidationException("Este curso já possui uma atividade com o mesmo enunciado.");
    }

    this.taskRepository
        .findTopByCourseIdOrderByOrderDesc(courseId)
        .ifPresent(
            lastTask -> {
              if (newOrder > lastTask.getOrder() + 1) {
                throw new ValidationException(
                    "A ordem da atividade está incorreta. A próxima ordem deveria ser "
                        + (lastTask.getOrder() + 1)
                        + ".");
              }
            });
  }

  private void validateChoiceOptions(List<TaskOptionDTO> options, String statement) {

    Set<String> uniqueOptions =
        options.stream().map(TaskOptionDTO::text).collect(Collectors.toSet());
    if (uniqueOptions.size() < options.size()) {
      throw new ValidationException("Não são permitidas alternativas com textos repetidos.");
    }

    if (uniqueOptions.stream().anyMatch(optionText -> optionText.equalsIgnoreCase(statement))) {
      throw new ValidationException("As alternativas não podem ser iguais ao enunciado.");
    }
  }

  private void validateSingleChoiceRules(List<TaskOptionDTO> options) {

    long correctOptionsCount = options.stream().filter(TaskOptionDTO::isCorrect).count();
    if (correctOptionsCount != 1) {
      throw new ValidationException(
          "Atividades de alternativa única devem ter exatamente uma opção correta.");
    }
  }

  private void validateMultipleChoiceRules(List<TaskOptionDTO> options) {
    long correctOptionsCount = options.stream().filter(TaskOptionDTO::isCorrect).count();
    long incorrectOptionsCount = options.size() - correctOptionsCount;

    if (correctOptionsCount < 2 || incorrectOptionsCount < 1) {
      throw new ValidationException(
          "Atividades de múltipla escolha devem ter 2 ou mais opções corretas e ao menos 1 incorreta.");
    }
  }

  private void handleTaskReordering(Long courseId, Integer newOrder) {
    this.taskRepository.incrementOrderFrom(courseId, newOrder);
  }
}
