package com.trenbologna.stronk.service.impl;

import com.trenbologna.stronk.domain.ExerciseEntity;
import com.trenbologna.stronk.domain.WorkoutEntity;
import com.trenbologna.stronk.repository.WorkoutRepository;
import com.trenbologna.stronk.service.WorkoutService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutServiceImpl(WorkoutRepository workoutRepository){
        this.workoutRepository = workoutRepository;
    }

    @Override
    public List<WorkoutEntity> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    @Override
    public WorkoutEntity getWorkout(Long id) {
        return workoutRepository.getReferenceById(id);
    }

    @Override
    @Transactional
    public WorkoutEntity createWorkout(WorkoutEntity req) {
        return workoutRepository.save(req);
    }

    @Override
    public void deleteWorkout(Long id) {
        workoutRepository.deleteById(id);
    }
}
