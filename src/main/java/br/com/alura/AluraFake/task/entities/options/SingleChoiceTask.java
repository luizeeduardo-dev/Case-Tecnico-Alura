package br.com.alura.AluraFake.task.entities.options;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.task.entities.TaskType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@DiscriminatorValue("SINGLE_CHOICE")
public class SingleChoiceTask extends ChoiceTask {

  public SingleChoiceTask() {
    super();
    this.setTaskType(TaskType.SINGLE_CHOICE);
  }
}
