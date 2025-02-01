package com.trenbologna.stronk.domain.workout_template.controller;

import com.trenbologna.stronk.domain.workout_template.dto.GetWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.dto.PutWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.dto.PostWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.service.WorkoutTemplateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workout-templates")
public class WorkoutTemplateController {
    private final WorkoutTemplateService workoutTemplateService;

    @Autowired
    public WorkoutTemplateController(WorkoutTemplateService workoutTemplateService){
            this.workoutTemplateService = workoutTemplateService;
    }
    @GetMapping("/")
    ResponseEntity<List<GetWorkoutTemplateDTO>> getAllWorkoutTemplates(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok(workoutTemplateService.getAllWorkouts(userDetails));
    }
    @GetMapping("/{id}")
    ResponseEntity<GetWorkoutTemplateDTO> getWorkoutTemplateById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ){
        return ResponseEntity.ok(workoutTemplateService.getWorkout(id, userDetails));
    }
    @PostMapping()
    ResponseEntity postWorkoutTemplate(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PostWorkoutTemplateDTO workoutDTO
    ){
        workoutTemplateService.createWorkout(workoutDTO, userDetails);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    ResponseEntity deleteWorkoutTemplate(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ){
        workoutTemplateService.deleteWorkout(id, userDetails);
        return ResponseEntity.ok().build();
    }

}
