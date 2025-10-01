package br.com.alura.AluraFake.course;

import java.time.LocalDateTime;

public record InstructorCourseItemDTO(
    Long id,
    String title,
    Status status,
    LocalDateTime publishedAt,
    Long taskCount
) {
  public InstructorCourseItemDTO(Course course) {
    this(course.getId(), course.getTitle(), course.getStatus(), course.getPublishedAt(), (long) course.getTasks().size());
  }
}

