package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.task.entities.OpenTextTask;
import br.com.alura.AluraFake.task.entities.Task;
import br.com.alura.AluraFake.task.entities.options.MultipleChoiceTask;
import br.com.alura.AluraFake.task.entities.options.SingleChoiceTask;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

  private final CourseRepository courseRepository;

  @Transactional
  public void publish(Long courseId) {
    Course course =
        this.courseRepository
            .findById(courseId)
            .orElseThrow(() -> new ValidationException("Curso não encontrado."));

    if (course.getStatus() != Status.BUILDING) {
      throw new ValidationException("O curso só pode ser publicado se o status for BUILDING.");
    }

    List<Task> tasks = course.getTasks();
    validateTaskTypes(tasks);
    validateTaskOrderSequence(tasks);

    course.publish();
  }

  private void validateTaskTypes(List<Task> tasks) {
    boolean hasOpenText = tasks.stream().anyMatch(OpenTextTask.class::isInstance);
    boolean hasSingleChoice = tasks.stream().anyMatch(SingleChoiceTask.class::isInstance);
    boolean hasMultipleChoice = tasks.stream().anyMatch(MultipleChoiceTask.class::isInstance);

    if (!hasOpenText || !hasSingleChoice || !hasMultipleChoice) {
      throw new ValidationException(
          "Para ser publicado, o curso deve conter ao menos uma atividade de cada tipo.");
    }
  }

  private void validateTaskOrderSequence(List<Task> tasks) {
    if (tasks.isEmpty()) {
      return;
    }

    tasks.sort(Comparator.comparing(Task::getOrder));

    for (int i = 0; i < tasks.size(); i++) {
      if (tasks.get(i).getOrder() != (i + 1)) {
        throw new ValidationException("A ordem das atividades não é uma sequência contínua.");
      }
    }
  }
}
