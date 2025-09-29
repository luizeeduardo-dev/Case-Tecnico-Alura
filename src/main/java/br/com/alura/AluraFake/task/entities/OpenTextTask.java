package br.com.alura.AluraFake.task.entities;

import br.com.alura.AluraFake.course.Course;
import jakarta.persistence.Entity;

public class OpenTextTask extends Task {

  public OpenTextTask(String statement, Integer order, Course course) {
    super(statement, order, course, TaskType.OPEN_TEXT);
  }
}
