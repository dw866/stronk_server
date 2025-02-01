package com.trenbologna.stronk.domain.workout_template.repository;

import com.trenbologna.stronk.domain.workout_template.WorkoutTemplateEntity;
import com.trenbologna.stronk.domain.workout_template.WorkoutTemplateExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutTemplateExerciseRepository extends JpaRepository<WorkoutTemplateExerciseEntity, Long> {
    List<WorkoutTemplateExerciseEntity> findAllByIdIn(List<Long> ids);
    List<WorkoutTemplateExerciseEntity> findByWorkoutTemplateId(Long id);
    List<WorkoutTemplateExerciseEntity> findByExerciseTemplateId(Long id);
    void deleteByWorkoutTemplate(WorkoutTemplateEntity workoutTemplate);
}
