package br.com.alura.AluraFake.task.repository;

import br.com.alura.AluraFake.task.entities.Task;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  boolean existsByCourseIdAndStatement(Long courseId, String statement);

  Optional<Task> findTopByCourseIdOrderByOrderDesc(Long courseId);

  @Modifying
  @Query(
      "UPDATE Task t SET t.order = t.order + 1 WHERE t.course.id = :courseId AND t.order >= :taskOrder")
  void incrementOrderFrom(
      @Param("courseId") Long courseId, @Param("taskOrder") Integer taskOrder);
}
