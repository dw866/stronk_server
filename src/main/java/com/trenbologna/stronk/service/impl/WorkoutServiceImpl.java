package com.trenbologna.stronk.service.impl;

import com.trenbologna.stronk.domain.ExerciseEntity;
import com.trenbologna.stronk.domain.WorkoutEntity;
import com.trenbologna.stronk.repository.ExerciseRepository;
import com.trenbologna.stronk.repository.WorkoutRepository;
import com.trenbologna.stronk.service.WorkoutService;
import jakarta.transaction.Transactional;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        if (req.getId() != null && workoutRepository.findById(req.getId()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Workout Entity Already Exists");
        }

        List<Long> exerciseIds = req.getExercises().stream()
                                    .map(ExerciseEntity::getId)
                                    .collect(Collectors.toList());
        List<ExerciseEntity> exerciseList = exerciseRepository.findAllById(exerciseIds);
        Map<Long, ExerciseEntity> exerciseMap = exerciseList.stream()
                                    .collect(Collectors.toMap(ExerciseEntity::getId, Function.identity()));

        Set<ExerciseEntity> exercises = new HashSet<>();
        for (Long exerciseId : exerciseIds) {
            ExerciseEntity exercise = exerciseMap.get(exerciseId);
            if (exercise == null) {
                throw new ResourceNotFoundException("Exercise not found with id " + exerciseId);
            }
            exercises.add(exercise);
        }

        WorkoutEntity workout = new WorkoutEntity();
        workout.setName(req.getName());
        workout.setNote(req.getNote());
        workout.setExercises(exercises);
        return workoutRepository.save(workout);
    }

    @Override
    @Transactional
    public WorkoutEntity patchWorkout(Long id, Map<String, Object> req) {
        WorkoutEntity workout = workoutRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Workout not found with id " + id));

        req.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(WorkoutEntity.class, key);
            if (field != null) {
                field.setAccessible(true);
                try {
                    Object convertedValue = ConvertUtils.convert(value, field.getType());
                    field.set(workout, convertedValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to update field: " + key, e);
                }
                field.setAccessible(false);
            } else {
                throw new RuntimeException("Field not found: " + key);
            }
        });

        return workoutRepository.save(workout);
    }

    @Override
    public void deleteWorkout(Long id) {
        workoutRepository.deleteById(id);
    }
}
