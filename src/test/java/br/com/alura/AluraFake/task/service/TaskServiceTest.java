package br.com.alura.AluraFake.task.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.task.dto.MultipleChoiceTaskDTO;
import br.com.alura.AluraFake.task.dto.OpenTextTaskDTO;
import br.com.alura.AluraFake.task.dto.SingleChoiceTaskDTO;
import br.com.alura.AluraFake.task.dto.TaskOptionDTO;
import br.com.alura.AluraFake.task.entities.OpenTextTask;
import br.com.alura.AluraFake.task.repository.TaskRepository;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @Mock private TaskRepository taskRepository;

  @Mock private CourseRepository courseRepository;

  @InjectMocks private TaskService taskService;

  private Course buildingCourse;
  private Course publishedCourse;
  private OpenTextTaskDTO openTextTaskDTO;
  private SingleChoiceTaskDTO singleChoiceTaskDTO;
  private MultipleChoiceTaskDTO multipleChoiceTaskDTO;

  @BeforeEach
  void setup() {
    User instructor = new User("Instructor", "instructor@test.com", Role.INSTRUCTOR, "pass123");
    this.buildingCourse = new Course("Java Basics", "Learn Java", instructor);

    this.publishedCourse = new Course("Advanced Java", "Advanced topics", instructor);
    this.publishedCourse.setStatus(Status.PUBLISHED);

    this.openTextTaskDTO = new OpenTextTaskDTO(this.buildingCourse.getId(), "What is Java?", 1);

    var options =
        List.of(
            new TaskOptionDTO("It's a language", true), new TaskOptionDTO("It's an island", false));
    this.singleChoiceTaskDTO =
        new SingleChoiceTaskDTO(this.buildingCourse.getId(), "What is Java?", 1, options);

    var multipleOptions =
        List.of(
            new TaskOptionDTO("It's a language", true),
            new TaskOptionDTO("It's an island", true),
            new TaskOptionDTO("It's a coffee", false));
    this.multipleChoiceTaskDTO =
        new MultipleChoiceTaskDTO(
            this.buildingCourse.getId(), "Select all that apply", 2, multipleOptions);
  }

  @Test
  @DisplayName("Deve criar uma OpenTextTask com sucesso")
  void createOpenTextTask_shouldSucceed() {
    when(this.courseRepository.findById(this.buildingCourse.getId()))
        .thenReturn(Optional.of(this.buildingCourse));
    when(this.taskRepository.existsByCourseIdAndStatement(any(), any())).thenReturn(false);

    assertDoesNotThrow(() -> this.taskService.createOpenTextTask(this.openTextTaskDTO));

    verify(this.taskRepository)
        .incrementOrderFrom(this.buildingCourse.getId(), this.openTextTaskDTO.order());
    verify(this.taskRepository).save(any(OpenTextTask.class));
  }

  @Test
  @DisplayName("Deve criar uma SingleChoiceTask com sucesso")
  void createSingleChoiceTask_shouldSucceed() {
    when(this.courseRepository.findById(this.buildingCourse.getId()))
        .thenReturn(Optional.of(this.buildingCourse));
    when(this.taskRepository.existsByCourseIdAndStatement(any(), any())).thenReturn(false);

    assertDoesNotThrow(() -> this.taskService.createSingleChoiceTask(this.singleChoiceTaskDTO));

    verify(this.taskRepository).save(any());
  }

  @Test
  @DisplayName("Deve criar uma MultipleChoiceTask com sucesso")
  void createMultipleChoiceTask_shouldSucceed() {
    when(this.courseRepository.findById(this.buildingCourse.getId()))
        .thenReturn(Optional.of(this.buildingCourse));
    when(this.taskRepository.existsByCourseIdAndStatement(any(), any())).thenReturn(false);

    assertDoesNotThrow(() -> this.taskService.createMultipleChoiceTask(this.multipleChoiceTaskDTO));

    verify(this.taskRepository).save(any());
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar criar tarefa em curso publicado")
  void createTask_shouldThrowException_whenCourseIsPublished() {
    var dto = new OpenTextTaskDTO(this.publishedCourse.getId(), "New task", 1);
    when(this.courseRepository.findById(this.publishedCourse.getId()))
        .thenReturn(Optional.of(this.publishedCourse));

    assertThrows(ValidationException.class, () -> this.taskService.createOpenTextTask(dto));

    verify(this.taskRepository, never()).save(any());
    verify(this.taskRepository, never()).incrementOrderFrom(any(), any());
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar criar tarefa com enunciado duplicado")
  void createTask_shouldThrowException_whenStatementIsDuplicate() {
    when(this.courseRepository.findById(this.buildingCourse.getId()))
        .thenReturn(Optional.of(this.buildingCourse));
    when(this.taskRepository.existsByCourseIdAndStatement(
            this.buildingCourse.getId(), this.openTextTaskDTO.statement()))
        .thenReturn(true);

    assertThrows(
        ValidationException.class, () -> this.taskService.createOpenTextTask(this.openTextTaskDTO));
  }

  @Test
  @DisplayName("Deve lançar exceção para SingleChoiceTask sem exatamente uma resposta correta")
  void createSingleChoiceTask_shouldThrowException_whenIncorrectNumberOfCorrectAnswers() {
    var invalidOptions =
        List.of(new TaskOptionDTO("Option A", true), new TaskOptionDTO("Option B", true));
    var invalidDto =
        new SingleChoiceTaskDTO(this.buildingCourse.getId(), "Invalid task", 1, invalidOptions);

    when(this.courseRepository.findById(this.buildingCourse.getId()))
        .thenReturn(Optional.of(this.buildingCourse));
    when(this.taskRepository.existsByCourseIdAndStatement(any(), any())).thenReturn(false);

    assertThrows(
        ValidationException.class, () -> this.taskService.createSingleChoiceTask(invalidDto));
  }
}
