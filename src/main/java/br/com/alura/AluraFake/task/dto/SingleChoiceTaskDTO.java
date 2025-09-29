package br.com.alura.AluraFake.task.dto;

import br.com.alura.AluraFake.task.entities.options.TaskOption;
import java.util.List;

public record SingleChoiceTaskDTO(Long courseId, String statement, Integer order, List<TaskOptionDTO> options) {}
