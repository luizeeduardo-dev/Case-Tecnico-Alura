package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.task.entities.Task;
import br.com.alura.AluraFake.user.User;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime createdAt = LocalDateTime.now();
  private String title;
  private String description;

  @ManyToOne private User instructor;

  @Enumerated(EnumType.STRING)
  private Status status;

  private LocalDateTime publishedAt;

  @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
  private List<Task> tasks;

  @Deprecated
  public Course() {}

  public Course(String title, String description, User instructor) {
    Assert.isTrue(instructor.isInstructor(), "Usuario deve ser um instrutor");
    this.title = title;
    this.instructor = instructor;
    this.description = description;
    this.status = Status.BUILDING;
  }

  public void publish() {
    this.status = Status.PUBLISHED;
    this.publishedAt = LocalDateTime.now();
  }
}
