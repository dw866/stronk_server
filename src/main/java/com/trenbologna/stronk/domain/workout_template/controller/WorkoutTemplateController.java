package com.trenbologna.stronk.domain.workout_template.controller;

import com.trenbologna.stronk.domain.workout_template.WorkoutTemplateEntity;
import com.trenbologna.stronk.domain.workout_template.dto.GetWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.dto.PutWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.dto.PostWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.service.WorkoutTemplateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TODO: Will need to include users
 *
 **/

@RestController
@RequestMapping("/api/v1/workout-templates")
public class WorkoutTemplateController {
    private final WorkoutTemplateService workoutTemplateService;

    @Autowired
    public WorkoutTemplateController(WorkoutTemplateService workoutTemplateService){
            this.workoutTemplateService = workoutTemplateService;
    }
    @GetMapping("/{id}")
    ResponseEntity<GetWorkoutTemplateDTO> getWorkoutTemplateById(@PathVariable Long id){
        return ResponseEntity.ok(workoutTemplateService.getWorkout(id));
    }
//    @GetMapping("/{userId}")
//    ResponseEntity<List<GetWorkoutDTO>> getAllWorkoutTemplatesByUserId(@PathVariable Long userId){
//        //todo
//        return ResponseEntity.badRequest().build();
//    }
    @PostMapping()
    ResponseEntity postWorkoutTemplate(@Valid @RequestBody PostWorkoutTemplateDTO workoutDTO){
        workoutTemplateService.createWorkout(workoutDTO);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}")
    ResponseEntity putWorkoutTemplate(@PathVariable Long id, @RequestBody PutWorkoutTemplateDTO workoutDTO){
        workoutTemplateService.putWorkout(id, workoutDTO);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    ResponseEntity deleteWorkoutTemplate(@PathVariable Long id){
        workoutTemplateService.deleteWorkout(id);
        return ResponseEntity.ok().build();
    }

}
