package br.com.alura.AluraFake.task.entities;

import br.com.alura.AluraFake.course.Course;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OPEN_TEXT")
public class OpenTextTask extends Task {

  protected OpenTextTask() {
    super();
    this.setTaskType(TaskType.OPEN_TEXT);
  }

  public OpenTextTask(String statement, Integer order, Course course) {
    super(statement, order, course, TaskType.OPEN_TEXT);
  }
}
