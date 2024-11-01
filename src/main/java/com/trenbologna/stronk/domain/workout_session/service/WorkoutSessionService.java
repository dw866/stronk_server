package com.trenbologna.stronk.domain.workout_session.service;

import com.trenbologna.stronk.domain.workout_session.dto.GetWorkoutSessionDTO;
import com.trenbologna.stronk.domain.workout_session.dto.PostWorkoutSessionDTO;

import java.util.List;

public interface WorkoutSessionService {
    GetWorkoutSessionDTO getWorkoutSession(Long id);

    List<GetWorkoutSessionDTO> getAllWorkoutSession();

    void performWorkoutSession(PostWorkoutSessionDTO postWorkoutSessionDTO);
}
