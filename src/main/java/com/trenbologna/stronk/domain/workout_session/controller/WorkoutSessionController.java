package com.trenbologna.stronk.domain.workout_session.controller;

import com.trenbologna.stronk.domain.workout_session.dto.PostWorkoutSessionDTO;
import com.trenbologna.stronk.domain.workout_session.service.WorkoutSessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workout-sessions")
public class WorkoutSessionController {
    @Autowired
    private final WorkoutSessionService workoutSessionService;

    public WorkoutSessionController(WorkoutSessionService workoutSessionService){
        this.workoutSessionService = workoutSessionService;
    }
    @GetMapping("/{id}")
    ResponseEntity getWorkoutSessionById(){
        //todo
        return ResponseEntity.badRequest().build();
    }
    @GetMapping("/{userId}")
    ResponseEntity getAllWorkoutSessionByUserId(){
        //todo
        return ResponseEntity.badRequest().build();
    }
    @PostMapping()
    ResponseEntity postWorkoutSession(@Valid @RequestBody PostWorkoutSessionDTO postWorkoutSessionDTO){
        //todo
        workoutSessionService.performWorkoutSession(postWorkoutSessionDTO);
        return ResponseEntity.ok().build();
    }
}
