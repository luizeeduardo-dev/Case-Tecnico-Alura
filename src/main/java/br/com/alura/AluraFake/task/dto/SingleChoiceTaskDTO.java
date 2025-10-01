package br.com.alura.AluraFake.task.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record SingleChoiceTaskDTO(
    Long courseId,
    @Size(min = 4, max = 255, message = "O enunciado deve ter entre 4 e 255 caracteres")
        String statement,
    @NotNull @Positive Integer order,
    @NotNull @Size(min = 2, max = 5, message = "O número de opções deve ser entre 2 e 5")
        List<TaskOptionDTO> options) {}
