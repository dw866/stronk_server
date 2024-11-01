package com.trenbologna.stronk.domain.workout_session.repository;

import com.trenbologna.stronk.domain.workout_session.WorkoutSessionExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutSessionExerciseRepository extends JpaRepository<WorkoutSessionExerciseEntity, Long> {
}
