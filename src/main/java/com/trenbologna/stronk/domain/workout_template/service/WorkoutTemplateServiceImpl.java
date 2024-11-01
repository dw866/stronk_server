package com.trenbologna.stronk.domain.workout_template.service;

import com.trenbologna.stronk.domain.workout_template.WorkoutTemplateExerciseEntity;
import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.workout_template.dto.PutWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.WorkoutTemplateEntity;
import com.trenbologna.stronk.domain.workout_template.dto.GetWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.dto.PostWorkoutTemplateDTO;
import com.trenbologna.stronk.domain.workout_template.mapper.GetWorkoutTemplateDtoMapper;
import com.trenbologna.stronk.domain.workout_template.mapper.PostWorkoutTemplateDtoMapper;
import com.trenbologna.stronk.domain.workout_template.mapper.PutWorkoutTemplateDtoMapper;
import com.trenbologna.stronk.domain.exercise.repository.ExerciseRepository;
import com.trenbologna.stronk.domain.workout_template.repository.WorkoutTemplateExerciseRepository;
import com.trenbologna.stronk.domain.workout_template.repository.WorkoutTemplateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkoutTemplateServiceImpl implements WorkoutTemplateService {
    private final WorkoutTemplateRepository workoutTemplateRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutTemplateExerciseRepository workoutTemplateExerciseRepository;

    @Autowired
    public WorkoutTemplateServiceImpl(WorkoutTemplateRepository workoutTemplateRepository, ExerciseRepository exerciseRepository, WorkoutTemplateExerciseRepository workoutTemplateExerciseRepository){
        this.workoutTemplateRepository = workoutTemplateRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutTemplateExerciseRepository = workoutTemplateExerciseRepository;
    }
    @Override
    @Transactional
    public GetWorkoutTemplateDTO getWorkout(Long id) {
        WorkoutTemplateEntity workout = workoutTemplateRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Workout not found with id " + id));
        return GetWorkoutTemplateDtoMapper.mapToDTO(workout, workoutTemplateExerciseRepository.findByWorkoutId(id));
    }
    @Override
    @Transactional
    public void createWorkout(PostWorkoutTemplateDTO workoutDTO) {
        WorkoutTemplateEntity workoutTemplate = PostWorkoutTemplateDtoMapper.mapFromDTO(workoutDTO);

        List<WorkoutTemplateExerciseEntity> workoutExerciseEntities = new ArrayList<>();
        for (PostWorkoutTemplateDTO.ExerciseDTO exerciseDTO: workoutDTO.getExercises()){
            //todo: find a way to optimize this with batch calls
            ExerciseEntity exercise = exerciseRepository.findById(exerciseDTO.getId())
                    .orElseThrow(()-> new ResourceNotFoundException("Exercise not found with id " + exerciseDTO.getId()));
            WorkoutTemplateExerciseEntity workoutTemplateExercise = WorkoutTemplateExerciseEntity.builder()
                    .exercise(exercise)
                    .workout(workoutTemplate)
                    .exerciseDetail(PostWorkoutTemplateDtoMapper.mapFromDTO(exerciseDTO))
                    .build();
            workoutExerciseEntities.add(workoutTemplateExercise);
        }
        workoutTemplate.setWorkoutExercises(workoutExerciseEntities.stream().collect(Collectors.toSet()));
        workoutTemplateRepository.save(workoutTemplate);
    }

    @Override
    @Transactional
    public void putWorkout(Long id, PutWorkoutTemplateDTO workoutDTO) {
        WorkoutTemplateEntity workoutTemplate = workoutTemplateRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Workout not found with id " + id))
                .setName(workoutDTO.getName())
                .setNote(workoutDTO.getNote());
        //Remove all associated workoutTemplateExercises
        workoutTemplate.getWorkoutExercises().clear();

        List<WorkoutTemplateExerciseEntity> workoutExerciseEntities = new ArrayList<>();
        for (PutWorkoutTemplateDTO.ExerciseDTO exerciseDTO: workoutDTO.getExercises()){
            ExerciseEntity exercise = exerciseRepository.findById(exerciseDTO.getId())
                    .orElseThrow(()-> new ResourceNotFoundException("Exercise not found with id " + exerciseDTO.getId()));
            WorkoutTemplateExerciseEntity workoutExercise = WorkoutTemplateExerciseEntity.builder()
                    .exercise(exercise)
                    .workout(workoutTemplate)
                    .exerciseDetail(PutWorkoutTemplateDtoMapper.mapFromDTO(exerciseDTO))
                    .build();
            workoutExerciseEntities.add(workoutExercise);
        }
        workoutTemplate.getWorkoutExercises().addAll(workoutExerciseEntities);
        workoutTemplateRepository.save(workoutTemplate);
    }

    @Override
    public void deleteWorkout(Long id) {
        //todo test if it only deletes workout or the associated workout exercises
        workoutTemplateRepository.deleteById(id);
    }
    //***************HELPER FUNCTIONS***************

}
