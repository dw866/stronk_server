package com.trenbologna.stronk.domain.workout_template.repository;

import com.trenbologna.stronk.domain.workout_template.WorkoutTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkoutTemplateRepository extends JpaRepository<WorkoutTemplateEntity, Long> {
    Optional<WorkoutTemplateEntity> findByIdAndUser_Email(Long id, String email);
    List<WorkoutTemplateEntity> findByUser_Email(String email);

}
