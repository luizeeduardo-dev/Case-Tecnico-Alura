package br.com.alura.AluraFake.task.entities;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.task.entities.options.TaskOption;
import jakarta.persistence.CascadeType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class ChoiceTask extends Task {

  public ChoiceTask(String statement, Integer order, Course course, TaskType taskType) {
    super(statement, order, course, taskType);
  }

  @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TaskOption> options = new ArrayList<>();
}
