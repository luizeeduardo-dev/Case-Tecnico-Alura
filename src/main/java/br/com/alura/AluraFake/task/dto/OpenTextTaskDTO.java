package br.com.alura.AluraFake.task.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record OpenTextTaskDTO(
    Long courseId,
    @NotNull @Size(min = 4, max = 255, message = "O enunciado deve ter entre 4 e 255 caracteres")
        String statement,
    @NotNull @Positive Integer order) {}
