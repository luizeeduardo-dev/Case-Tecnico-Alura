package br.com.alura.AluraFake.task.entities;

import br.com.alura.AluraFake.course.Course;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    name = "tasks",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"course_id", "statement"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  private String statement;

  @Column(name = "task_order")
  private Integer order;

  @Column(name = "type", insertable = false, updatable = false)
  @Enumerated(EnumType.STRING)
  private TaskType taskType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id")
  private Course course;

  protected Task(String statement, Integer order, TaskType taskType, Course course) {
    this.statement = statement;
    this.order = order;
    this.taskType = taskType;
    this.course = course;
  }
}
