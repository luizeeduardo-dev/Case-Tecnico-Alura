package br.com.alura.AluraFake.course;

import java.util.List;

public record InstructorReportDTO(
    List<InstructorCourseItemDTO> courses, long totalPublishedCourses) {}
