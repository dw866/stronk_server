package com.trenbologna.stronk.domain.exercise.controller;

import com.trenbologna.stronk.domain.exercise.dto.GetExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PostExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PatchExerciseDTO;
import com.trenbologna.stronk.domain.exercise.service.ExerciseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    ResponseEntity<List<GetExerciseDTO>> getExercises(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(exerciseService.getAllExercises(userDetails));
    }
    @PostMapping()
    ResponseEntity postExercise(@AuthenticationPrincipal UserDetails userDetails,
                                @Valid @RequestBody PostExerciseDTO postExerciseDTO){
        exerciseService.createExercise(postExerciseDTO, userDetails);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{id}")
    ResponseEntity patchExercise(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable Long id,
                                 @RequestBody PatchExerciseDTO patchExerciseDTO){
        exerciseService.patchExercise(id, patchExerciseDTO, userDetails);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    ResponseEntity deleteExercise(@AuthenticationPrincipal UserDetails userDetails,
                                  @PathVariable Long id){
        exerciseService.deleteExercise(id, userDetails);
        return ResponseEntity.ok().build();
    }
}
