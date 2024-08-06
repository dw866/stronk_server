package com.trenbologna.stronk.service;

import com.trenbologna.stronk.domain.ExerciseEntity;

import java.util.List;

public interface ExerciseService {
    public List<ExerciseEntity> getAllExercises();
    public ExerciseEntity getExercise(Long id);
    public ExerciseEntity createExercise(ExerciseEntity req);
    public void deleteExercise(Long id);
}
