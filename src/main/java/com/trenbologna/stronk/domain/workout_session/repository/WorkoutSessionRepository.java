package com.trenbologna.stronk.domain.workout_session.repository;

import com.trenbologna.stronk.domain.workout_session.WorkoutSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSessionEntity, Long> {

}
