package com.trenbologna.stronk.domain.workout_template.service;

import com.trenbologna.stronk.domain.workout_template.dto.GetWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.dto.PutWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.dto.PostWorkoutTemplateDTO;

public interface WorkoutTemplateService {
    public GetWorkoutTemplateDTO getWorkout(Long id);
    public void createWorkout(PostWorkoutTemplateDTO workoutDTO);
    public void putWorkout(Long id, PutWorkoutTemplateDTO workoutDTO);
    public void deleteWorkout(Long id);
}
