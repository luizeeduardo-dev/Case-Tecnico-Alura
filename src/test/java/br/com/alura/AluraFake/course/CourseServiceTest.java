package br.com.alura.AluraFake.course;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import br.com.alura.AluraFake.infra.exception.ValidationException;
import br.com.alura.AluraFake.task.entities.OpenTextTask;
import br.com.alura.AluraFake.task.entities.Task;
import br.com.alura.AluraFake.task.entities.options.MultipleChoiceTask;
import br.com.alura.AluraFake.task.entities.options.SingleChoiceTask;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import br.com.alura.AluraFake.user.UserRepository;
import java.util.ArrayList;
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
class CourseServiceTest {

  @Mock private CourseRepository courseRepository;
  @Mock private UserRepository userRepository;

  @InjectMocks private CourseService courseService;

  private Course course;
  private User instructor;

  @BeforeEach
  void setup() {
    this.instructor = new User("Instructor", "instructor@test.com", Role.INSTRUCTOR, "pass123");
    this.course = new Course("Java Basics", "Learn Java", this.instructor);
    this.course.setId(1L);
  }

  @Test
  @DisplayName("Deve publicar um curso com sucesso quando todas as regras são atendidas")
  void publish_shouldSucceed_whenAllRulesAreMet() {
    List<Task> tasks = new ArrayList<>();
    tasks.add(new OpenTextTask("Task 1", 1, this.course));
    tasks.add(new SingleChoiceTask("Task 2", 2, this.course, null));
    tasks.add(new MultipleChoiceTask("Task 3", 3, this.course, null));
    this.course.setTasks(tasks);

    when(this.courseRepository.findById(1L)).thenReturn(Optional.of(this.course));

    assertDoesNotThrow(() -> this.courseService.publish(1L));

    assertEquals(Status.PUBLISHED, this.course.getStatus());
    assertNotNull(this.course.getPublishedAt());
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar publicar um curso que não está com status BUILDING")
  void publish_shouldThrowException_whenCourseIsNotBuilding() {
    this.course.setStatus(Status.PUBLISHED);
    when(this.courseRepository.findById(1L)).thenReturn(Optional.of(this.course));

    ValidationException exception =
        assertThrows(ValidationException.class, () -> this.courseService.publish(1L));
    assertEquals("O curso só pode ser publicado se o status for BUILDING.", exception.getMessage());
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar publicar um curso sem todos os tipos de tarefas")
  void publish_shouldThrowException_whenMissingTaskTypes() {
    List<Task> tasks = new ArrayList<>();
    tasks.add(new OpenTextTask("Task 1", 1, this.course));
    this.course.setTasks(tasks);

    when(this.courseRepository.findById(1L)).thenReturn(Optional.of(this.course));

    ValidationException exception =
        assertThrows(ValidationException.class, () -> this.courseService.publish(1L));
    assertEquals(
        "Para ser publicado, o curso deve conter ao menos uma atividade de cada tipo.",
        exception.getMessage());
  }

  @Test
  @DisplayName(
      "Deve lançar exceção ao tentar publicar um curso com ordem de tarefas não sequencial")
  void publish_shouldThrowException_whenTaskOrderIsNotSequential() {
    List<Task> tasks = new ArrayList<>();
    tasks.add(new OpenTextTask("Task 1", 1, this.course));
    tasks.add(new SingleChoiceTask("Task 2", 3, this.course, null));
    tasks.add(new MultipleChoiceTask("Task 3", 4, this.course, null));
    this.course.setTasks(tasks);

    when(this.courseRepository.findById(1L)).thenReturn(Optional.of(this.course));

    ValidationException exception =
        assertThrows(ValidationException.class, () -> this.courseService.publish(1L));
    assertEquals("A ordem das atividades não é uma sequência contínua.", exception.getMessage());
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar publicar um curso que não existe")
  void publish_shouldThrowException_whenCourseNotFound() {
    when(this.courseRepository.findById(99L)).thenReturn(Optional.empty());

    ValidationException exception =
        assertThrows(ValidationException.class, () -> this.courseService.publish(99L));
    assertEquals("Curso não encontrado.", exception.getMessage());
  }
}
