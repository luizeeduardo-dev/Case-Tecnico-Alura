package br.com.alura.AluraFake.task.entities.options;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.task.entities.Task;
import br.com.alura.AluraFake.task.entities.TaskType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class ChoiceTask extends Task {

  @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TaskOption> options = new ArrayList<>();
}
