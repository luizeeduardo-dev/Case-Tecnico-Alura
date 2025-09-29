package br.com.alura.AluraFake.task.service;

import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.task.entities.OpenTextTask;
import br.com.alura.AluraFake.task.repository.TaskRepository;
import br.com.alura.AluraFake.task.entities.TaskType;
import br.com.alura.AluraFake.task.entities.options.SingleChoiceTask;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

  private final TaskRepository taskRepository;
  private final CourseRepository courseRepository;

  public TaskService(TaskRepository taskRepository, CourseRepository courseRepository) {
    this.taskRepository = taskRepository;
    this.courseRepository = courseRepository;
  }

  public void createOpenTextTask(String statement, Integer order, Long courseId) {
    var course =
        this.courseRepository
            .findById(courseId)
            .orElseThrow(() -> new IllegalArgumentException("Course not found"));

    OpenTextTask task = new OpenTextTask(statement, order, course);
    this.taskRepository.save(task);
  }

  public void createSingleChoiceTask(String statement, Integer order, Long courseId) {
    var course =
        this.courseRepository
            .findById(courseId)
            .orElseThrow(() -> new IllegalArgumentException("Course not found"));

    SingleChoiceTask task = new SingleChoiceTask(statement, order, course, TaskType.SINGLE_CHOICE);
    this.taskRepository.save(task);
  }

  public void createMultipleChoiceTask(String statement, Integer order, Long courseId) {
    var course =
        this.courseRepository
            .findById(courseId)
            .orElseThrow(() -> new IllegalArgumentException("Course not found"));

    SingleChoiceTask task = new SingleChoiceTask(statement, order, course, TaskType.MULTIPLE_CHOICE);
    this.taskRepository.save(task);
  }
}
