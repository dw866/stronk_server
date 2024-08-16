package com.trenbologna.stronk.service.impl;

import com.trenbologna.stronk.domain.ExerciseEntity;
import com.trenbologna.stronk.repository.ExerciseRepository;
import com.trenbologna.stronk.service.ExerciseService;
import jakarta.transaction.Transactional;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.el.util.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

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
    @Transactional
    public ExerciseEntity createExercise(ExerciseEntity req) {
        if (exerciseRepository.findById(req.getId()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Exercise Entity Already Exists");
        }
        return exerciseRepository.save(req);
    }

    @Override
    @Transactional
    public ExerciseEntity patchExercise(Long id, Map<String, Object> req) {
        ExerciseEntity exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with id " + id));

        req.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(ExerciseEntity.class, key);
            if (field != null) {
                field.setAccessible(true);
                try {
                    Object convertedValue = ConvertUtils.convert(value, field.getType());
                    field.set(exercise, convertedValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to update field: " + key, e);
                }
                field.setAccessible(false);
            } else {
                throw new RuntimeException("Field not found: " + key);
            }
        });

        return exerciseRepository.save(exercise);
    }


    @Override
    public void deleteExercise(Long id) {
        exerciseRepository.deleteById(id);
    }
}
