package com.trenbologna.stronk.service;

import com.trenbologna.stronk.domain.ExerciseEntity;
import com.trenbologna.stronk.domain.WorkoutEntity;
import java.util.List;
import java.util.Map;

public interface WorkoutService {
    public List<WorkoutEntity> getAllWorkouts();
    public WorkoutEntity getWorkout(Long id);
    public WorkoutEntity createWorkout(WorkoutEntity req);
    public WorkoutEntity patchWorkout(Long id, Map<String, Object> req);
    public void deleteWorkout(Long id);

}
