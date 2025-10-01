package br.com.alura.AluraFake.task.repository;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.task.entities.OpenTextTask;
import br.com.alura.AluraFake.task.entities.Task;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest {

  @Autowired private TestEntityManager em;

  @Autowired private TaskRepository taskRepository;

  private Course course;

  @BeforeEach
  void setup() {
    User instructor = new User("Instructor", "instructor@test.com", Role.INSTRUCTOR, "senha123");
    this.em.persist(instructor);

    this.course = new Course("Java Basics", "Learn Java from scratch", instructor);
    this.em.persist(this.course);
  }

  @Test
  @DisplayName("Deve retornar true quando existe uma tarefa com o mesmo enunciado no curso")
  void existsByCourseIdAndStatement_shouldReturnTrueWhenTaskExists() {
    String statement = "What is JVM?";
    this.em.persist(new OpenTextTask(statement, 1, this.course));

    boolean exists =
        this.taskRepository.existsByCourseIdAndStatement(this.course.getId(), statement);

    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName("Deve retornar false quando não existe tarefa com o mesmo enunciado no curso")
  void existsByCourseIdAndStatement_shouldReturnFalseWhenTaskDoesNotExist() {
    String statement = "What is JVM?";

    boolean exists =
        this.taskRepository.existsByCourseIdAndStatement(this.course.getId(), statement);

    assertThat(exists).isFalse();
  }

  @Test
  @DisplayName("Deve retornar a última tarefa ordenada pela ordem decrescente")
  void findTopByCourseIdOrderByOrderDesc_shouldReturnLastTask() {
    this.em.persist(new OpenTextTask("Task 1", 1, this.course));
    Task lastTask = new OpenTextTask("Task 2", 2, this.course);
    this.em.persist(lastTask);

    Optional<Task> foundTask =
        this.taskRepository.findTopByCourseIdOrderByOrderDesc(this.course.getId());

    assertThat(foundTask).isPresent();
    assertThat(foundTask.get().getOrder()).isEqualTo(2);
    assertThat(foundTask.get().getId()).isEqualTo(lastTask.getId());
  }

  @Test
  @DisplayName("Deve retornar Optional vazio quando o curso não tem tarefas")
  void findTopByCourseIdOrderByOrderDesc_shouldReturnEmptyWhenNoTasks() {
    Optional<Task> foundTask =
        this.taskRepository.findTopByCourseIdOrderByOrderDesc(this.course.getId());

    assertThat(foundTask).isEmpty();
  }

  @Test
  @DisplayName("Deve incrementar a ordem das tarefas a partir da ordem inicial especificada")
  void incrementOrderFrom_shouldShiftTasksOrder() {
    Task task1 = new OpenTextTask("Task 1", 1, this.course);
    Task task2 = new OpenTextTask("Task 2", 2, this.course);
    Task task3 = new OpenTextTask("Task 3", 3, this.course);
    this.em.persist(task1);
    this.em.persist(task2);
    this.em.persist(task3);

    this.taskRepository.incrementOrderFrom(this.course.getId(), 2);
    this.em.flush();
    this.em.clear();

    List<Task> updatedTasks =
        this.taskRepository.findAllById(List.of(task1.getId(), task2.getId(), task3.getId()));

    assertThat(updatedTasks).hasSize(3);
    assertThat(updatedTasks).extracting(Task::getOrder).containsExactlyInAnyOrder(1, 3, 4);
  }
}
