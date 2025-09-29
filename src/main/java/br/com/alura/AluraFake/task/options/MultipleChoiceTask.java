package br.com.alura.AluraFake.task.options;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.task.ChoiceTask;
import br.com.alura.AluraFake.task.Task;
import br.com.alura.AluraFake.task.TaskType;

public class MultipleChoiceTask extends ChoiceTask {

  public MultipleChoiceTask(String statement, Integer order, Course course, TaskType taskType) {
    super(statement, order, course, TaskType.MULTIPLE_CHOICE);
  }
}
