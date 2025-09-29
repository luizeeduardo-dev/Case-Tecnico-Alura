package br.com.alura.AluraFake.task.dto;

import java.util.List;

public record MultipleChoiceTaskDTO(
    Long courseId, String statement, Integer order, List<TaskOptionDTO> options) {}
