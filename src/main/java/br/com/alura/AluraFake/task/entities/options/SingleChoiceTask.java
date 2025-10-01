package br.com.alura.AluraFake.task.entities.options;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.task.entities.TaskType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SINGLE_CHOICE")
public class SingleChoiceTask extends ChoiceTask {

  protected SingleChoiceTask() {
    super();
    this.setTaskType(TaskType.SINGLE_CHOICE);
  }

  public SingleChoiceTask(
      String statement, Integer order, Course course, java.util.List<TaskOption> options) {
    super(options);
    this.setStatement(statement);
    this.setOrder(order);
    this.setCourse(course);
    this.setTaskType(TaskType.SINGLE_CHOICE);
  }
}
