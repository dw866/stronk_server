package com.trenbologna.stronk.controller;

import com.trenbologna.stronk.domain.ExerciseEntity;
import com.trenbologna.stronk.domain.WorkoutEntity;
import com.trenbologna.stronk.domain.dto.ExerciseDTO;
import com.trenbologna.stronk.domain.dto.WorkoutDTO;
import com.trenbologna.stronk.mapper.Mapper;
import com.trenbologna.stronk.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO: Will need to include users
 *
 **/

@RestController
@RequestMapping("/api/v1/workouts")
public class WorkoutController {
    @Autowired
    private WorkoutService workoutService;

    private Mapper<WorkoutEntity, WorkoutDTO> workoutMapper;
    public WorkoutController(WorkoutService workoutService, Mapper<WorkoutEntity, WorkoutDTO> workoutMapper){
            this.workoutService = workoutService;
            this.workoutMapper = workoutMapper;
    }

    @GetMapping()
    ResponseEntity<List<WorkoutDTO>> getExercises(){
        if (workoutService.getAllWorkouts() == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(workoutMapper.mapTo(workoutService.getAllWorkouts()));
    }
    @GetMapping("/{id}")
    ResponseEntity<WorkoutDTO> getWorkoutByID(@PathVariable Long id){
        return ResponseEntity.ok(workoutMapper.mapTo(workoutService.getWorkout(id)));
    }

    @PostMapping()
    ResponseEntity<WorkoutDTO> postWorkout(@Valid @RequestBody WorkoutDTO workoutDTO){
        WorkoutEntity workoutEntity = workoutMapper.mapFrom(workoutDTO);
        WorkoutEntity savedWorkoutEntity = workoutService.createWorkout(workoutEntity);
        return ResponseEntity.ok(workoutMapper.mapTo(savedWorkoutEntity));
    }

    @PatchMapping("/{id}")
    ResponseEntity<WorkoutDTO> patchWorkout(@PathVariable Long id, @RequestBody Map<String, Object> fields){
        fields.remove("id");
        return ResponseEntity.ok(workoutMapper.mapTo(workoutService.patchWorkout(id, fields)));
    }

}
