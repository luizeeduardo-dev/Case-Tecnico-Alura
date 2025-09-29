package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;

public class OpenTextTask extends Task {

  public OpenTextTask(String statement, Integer order, Course course) {
    super(statement, order, course, TaskType.OPEN_TEXT);
  }
}
