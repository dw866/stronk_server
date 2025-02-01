package com.trenbologna.stronk.domain.workout_session.controller;

import com.trenbologna.stronk.domain.workout_session.dto.GetWorkoutSessionDTO;
import com.trenbologna.stronk.domain.workout_session.dto.PostWorkoutSessionDTO;
import com.trenbologna.stronk.domain.workout_session.service.WorkoutSessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workout-sessions")
public class WorkoutSessionController {
    @Autowired
    private final WorkoutSessionService workoutSessionService;

    public WorkoutSessionController(WorkoutSessionService workoutSessionService){
        this.workoutSessionService = workoutSessionService;
    }
    @GetMapping("/{userId}")
    ResponseEntity<List<GetWorkoutSessionDTO>> getWorkoutSessions(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok(workoutSessionService.getWorkoutSessions(userDetails));
    }
    @PostMapping()
    ResponseEntity postWorkoutSession(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PostWorkoutSessionDTO postWorkoutSessionDTO
    ){
        workoutSessionService.performWorkoutSession(postWorkoutSessionDTO, userDetails);
        return ResponseEntity.ok().build();
    }
}
