package com.trenbologna.stronk.service.impl;

import com.trenbologna.stronk.domain.ExerciseEntity;
import com.trenbologna.stronk.repository.ExerciseRepository;
import com.trenbologna.stronk.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseServiceImpl(ExerciseRepository exerciseRepository){
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public List<ExerciseEntity> getAllExercises() {
        return exerciseRepository.findAll();
    }

    @Override
    public ExerciseEntity getExercise(Long id) {
        return exerciseRepository.getReferenceById(id);
    }

    @Override
    public ExerciseEntity createExercise(ExerciseEntity exercise) {
        return exerciseRepository.save(exercise);
    }

    @Override
    public void deleteExercise(Long id) {
        exerciseRepository.deleteById(id);
    }
}
