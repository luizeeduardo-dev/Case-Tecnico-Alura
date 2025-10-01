package br.com.alura.AluraFake.task.entities;

import br.com.alura.AluraFake.course.Course;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("OPEN_TEXT")
public class OpenTextTask extends Task {

  public OpenTextTask() {
    super();
    this.setTaskType(TaskType.OPEN_TEXT);
  }
}
