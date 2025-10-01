package br.com.alura.AluraFake.task.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.alura.AluraFake.task.dto.OpenTextTaskDTO;
import br.com.alura.AluraFake.task.dto.SingleChoiceTaskDTO;
import br.com.alura.AluraFake.task.dto.TaskOptionDTO;
import br.com.alura.AluraFake.task.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private TaskService taskService;

  @Test
  @DisplayName("Deve retornar 201 Created ao criar uma OpenTextTask com dados válidos")
  void createOpenTextTask_whenValidInput_thenReturns201Created() throws Exception {
    var taskDto = new OpenTextTaskDTO(1L, "What is Dependency Injection?", 1);

    doNothing().when(this.taskService).createOpenTextTask(any(OpenTextTaskDTO.class));

    this.mockMvc
        .perform(
            post("/task/new/opentext")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(taskDto)))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("Deve retornar 201 Created ao criar uma SingleChoiceTask com dados válidos")
  void createSingleChoiceTask_whenValidInput_thenReturns201Created() throws Exception {
    var options = List.of(new TaskOptionDTO("Java", true), new TaskOptionDTO("Python", false));
    var taskDto =
        new SingleChoiceTaskDTO(1L, "Which language is this project written in?", 2, options);

    doNothing().when(this.taskService).createSingleChoiceTask(any(SingleChoiceTaskDTO.class));

    this.mockMvc
        .perform(
            post("/task/new/singlechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(taskDto)))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("Deve retornar 201 Created ao criar uma MultipleChoiceTask com dados válidos")
  void createMultipleChoiceTask_whenValidInput_thenReturns201Created() throws Exception {

    var options =
        List.of(
            new TaskOptionDTO("Java", true),
            new TaskOptionDTO("Spring", true),
            new TaskOptionDTO("Mysql", true),
            new TaskOptionDTO("RabbitMQ", false),
            new TaskOptionDTO("MongoDB", false));
    var taskDto =
        new SingleChoiceTaskDTO(
            1L, "What technologies are being used in this project?", 2, options);

    doNothing().when(this.taskService).createSingleChoiceTask(any(SingleChoiceTaskDTO.class));

    this.mockMvc
        .perform(
            post("/task/new/multiplechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(taskDto)))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("Deve retornar 400 Bad Request ao tentar criar uma tarefa com dados inválidos")
  void createTask_whenInvalidInput_thenReturns400BadRequest() throws Exception {
    var invalidDto = new OpenTextTaskDTO(1L, "Hi", 0);

    this.mockMvc
        .perform(
            post("/task/new/opentext")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(invalidDto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[*].field", containsInAnyOrder("statement", "order")));
  }
}
