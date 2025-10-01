package br.com.alura.AluraFake.task.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskOptionDTO(
    @NotNull @Size(min = 4, max = 80, message = "A opção deve ter entre 4 e 80 caracteres")
        String text,
    boolean isCorrect) {}
