package com.trenbologna.stronk.controller;

import com.trenbologna.stronk.domain.ExerciseEntity;
import com.trenbologna.stronk.domain.dto.ExerciseDTO;
import com.trenbologna.stronk.mapper.Mapper;
import com.trenbologna.stronk.service.ExerciseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * TODO: Will need to include users
 *
 **/

@RestController
@RequestMapping("/api/v1/exercises")
public class ExerciseController {
    @Autowired
    private ExerciseService exerciseService;
    private Mapper<ExerciseEntity, ExerciseDTO> exerciseMapper;

    public ExerciseController(ExerciseService exerciseService, Mapper<ExerciseEntity, ExerciseDTO> exerciseMapper){
        this.exerciseService = exerciseService;
        this.exerciseMapper = exerciseMapper;
    }

    @GetMapping()
    ResponseEntity<List<ExerciseDTO>> getExercises(){
        if (exerciseService.getAllExercises() == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(exerciseMapper.mapTo(exerciseService.getAllExercises()));
    }
    @GetMapping("/{id}")
    ResponseEntity<ExerciseDTO> getExercisesByID(@PathVariable Long id){
        return ResponseEntity.ok(exerciseMapper.mapTo(exerciseService.getExercise(id)));
    }
    @PostMapping()
    ResponseEntity<ExerciseDTO> postExercise(@Valid @RequestBody ExerciseDTO exerciseDTO){
        ExerciseEntity exerciseEntity = exerciseMapper.mapFrom(exerciseDTO);
        ExerciseEntity savedExerciseEntity = exerciseService.createExercise(exerciseEntity);
        return ResponseEntity.ok(exerciseMapper.mapTo(savedExerciseEntity));
    }
    @PatchMapping("/{id}")
    ResponseEntity<ExerciseDTO> patchExercise(@PathVariable Long id, @RequestBody Map<String, Object> fields){
        fields.remove("id");
        return ResponseEntity.ok(exerciseMapper.mapTo(exerciseService.patchExercise(id, fields)));
    }
    @DeleteMapping("/{id}")
    ResponseEntity deleteExercise(@PathVariable Long id){
        exerciseService.deleteExercise(id);
        return ResponseEntity.ok().build();
    }
}
