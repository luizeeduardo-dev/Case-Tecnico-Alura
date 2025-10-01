package br.com.alura.AluraFake.course;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long>{

  @Query("SELECT c FROM Course c LEFT JOIN FETCH c.tasks WHERE c.instructor.id = :instructorId")
  List<Course> findAllByInstructorIdWithTasks(Long instructorId);
}
