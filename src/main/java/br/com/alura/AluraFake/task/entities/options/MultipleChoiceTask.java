package br.com.alura.AluraFake.task.entities.options;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.task.entities.ChoiceTask;
import br.com.alura.AluraFake.task.entities.TaskType;
import jakarta.persistence.Entity;

public class MultipleChoiceTask extends ChoiceTask {

  public MultipleChoiceTask(String statement, Integer order, Course course, TaskType taskType) {
    super(statement, order, course, TaskType.MULTIPLE_CHOICE);
  }
}
