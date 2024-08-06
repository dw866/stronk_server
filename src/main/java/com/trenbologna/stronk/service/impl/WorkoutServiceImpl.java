package com.trenbologna.stronk.service.impl;

import com.trenbologna.stronk.domain.ExerciseEntity;
import com.trenbologna.stronk.domain.WorkoutEntity;
import com.trenbologna.stronk.repository.ExerciseRepository;
import com.trenbologna.stronk.repository.WorkoutRepository;
import com.trenbologna.stronk.service.WorkoutService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public WorkoutServiceImpl(WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository){
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
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
        WorkoutEntity workout = new WorkoutEntity();
        workout.setName(req.getName());

        List<ExerciseEntity> exercises = new ArrayList<>();
        for (ExerciseEntity detachedExercise : req.getExercises()) {
            ExerciseEntity exercise = exerciseRepository.findById(detachedExercise.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with id " + detachedExercise.getId()));
            exercises.add(exercise);
        }
        workout.setExercises(exercises);

        return workoutRepository.save(workout);
    }

    @Override
    public void deleteWorkout(Long id) {
        workoutRepository.deleteById(id);
    }
}
