package com.trenbologna.stronk.domain.exercise.controller;

import com.trenbologna.stronk.domain.exercise.dto.GetExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PostExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PatchExerciseDTO;
import com.trenbologna.stronk.domain.exercise.service.ExerciseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * TODO: Will need to include users
 *
 **/

@RestController
@RequestMapping("/api/v1/exercises")
public class ExerciseController {
    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService){
        this.exerciseService = exerciseService;
    }

    @GetMapping()
    ResponseEntity<List<GetExerciseDTO>> getExercises(){
        return ResponseEntity.ok(exerciseService.getAllExercises());
    }
    @GetMapping("/{id}")
    ResponseEntity<GetExerciseDTO> getExercisesByID(@PathVariable Long id){
        return ResponseEntity.ok(exerciseService.getExercise(id));
    }
    @PostMapping()
    ResponseEntity postExercise(@Valid @RequestBody PostExerciseDTO postExerciseDTO){
        exerciseService.createExercise(postExerciseDTO);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{id}")
    ResponseEntity patchExercise(@PathVariable Long id, @RequestBody PatchExerciseDTO patchExerciseDTO){
        exerciseService.patchExercise(id, patchExerciseDTO);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    ResponseEntity deleteExercise(@PathVariable Long id){
        exerciseService.deleteExercise(id);
        return ResponseEntity.ok().build();
    }
}
