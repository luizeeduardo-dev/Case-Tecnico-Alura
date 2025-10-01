package br.com.alura.AluraFake.task.controller;

import br.com.alura.AluraFake.task.dto.MultipleChoiceTaskDTO;
import br.com.alura.AluraFake.task.dto.OpenTextTaskDTO;
import br.com.alura.AluraFake.task.dto.SingleChoiceTaskDTO;
import br.com.alura.AluraFake.task.entities.OpenTextTask;
import br.com.alura.AluraFake.task.entities.options.MultipleChoiceTask;
import br.com.alura.AluraFake.task.entities.options.SingleChoiceTask;
import br.com.alura.AluraFake.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService service) {
    this.taskService = service;
  }

  @PostMapping("/new/opentext")
  public ResponseEntity<OpenTextTask> newOpenTextExercise(
      @RequestBody @Valid OpenTextTaskDTO taskDto) {
    this.taskService.createOpenTextTask(taskDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/new/singlechoice")
  public ResponseEntity<SingleChoiceTask> newSingleChoice(
      @RequestBody @Valid SingleChoiceTaskDTO taskDTO) {
    this.taskService.createSingleChoiceTask(taskDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/new/multiplechoice")
  public ResponseEntity<MultipleChoiceTask> newMultipleChoice(
      @RequestBody @Valid MultipleChoiceTaskDTO taskDTO) {
    this.taskService.createMultipleChoiceTask(taskDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
