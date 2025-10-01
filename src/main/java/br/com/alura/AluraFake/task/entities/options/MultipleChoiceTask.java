package br.com.alura.AluraFake.task.entities.options;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.task.entities.TaskType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
public class MultipleChoiceTask extends ChoiceTask {

  protected MultipleChoiceTask() {}

  public MultipleChoiceTask(
      String statement, Integer order, Course course, List<TaskOption> options) {
    super(options);
    this.setStatement(statement);
    this.setOrder(order);
    this.setCourse(course);
    this.setTaskType(TaskType.MULTIPLE_CHOICE);
  }
}
