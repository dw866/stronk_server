package com.trenbologna.stronk.repository;

import com.trenbologna.stronk.domain.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<WorkoutEntity, Long> {
}
