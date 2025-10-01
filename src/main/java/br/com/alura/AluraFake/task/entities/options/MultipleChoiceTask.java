package br.com.alura.AluraFake.task.entities.options;

import br.com.alura.AluraFake.task.entities.TaskType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import lombok.Data;

@Data
@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
public class MultipleChoiceTask extends ChoiceTask {

  public MultipleChoiceTask() {
    super();
    this.setTaskType(TaskType.MULTIPLE_CHOICE);
  }
}
