package com.trenbologna.stronk.service;

import com.trenbologna.stronk.domain.ExerciseEntity;

import java.util.List;
import java.util.Map;

public interface ExerciseService {
    public List<ExerciseEntity> getAllExercises();
    public ExerciseEntity getExercise(Long id);
    public ExerciseEntity createExercise(ExerciseEntity req);
    public ExerciseEntity patchExercise(Long id, Map<String, Object> req);
    public void deleteExercise(Long id);
}
