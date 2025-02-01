package com.trenbologna.stronk.domain.workout_template.service;

import com.trenbologna.stronk.domain.exercise.dto.GetExerciseDTO;
import com.trenbologna.stronk.domain.user.UserEntity;
import com.trenbologna.stronk.domain.user.repository.UserRepository;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkoutTemplateService {
    private final WorkoutTemplateRepository workoutTemplateRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutTemplateExerciseRepository workoutTemplateExerciseRepository;
    private final UserRepository userRepository;

    @Autowired
    public WorkoutTemplateService(WorkoutTemplateRepository workoutTemplateRepository,
                                  ExerciseRepository exerciseRepository,
                                  WorkoutTemplateExerciseRepository workoutTemplateExerciseRepository,
                                  UserRepository userRepository){
        this.workoutTemplateRepository = workoutTemplateRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutTemplateExerciseRepository = workoutTemplateExerciseRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public List<GetWorkoutTemplateDTO> getAllWorkouts(UserDetails userDetails){
        List<WorkoutTemplateEntity> workoutTemplates = workoutTemplateRepository.findByUser_Email(userDetails.getUsername());
        return workoutTemplates.stream().map(e->{
            List<WorkoutTemplateExerciseEntity> workoutTemplateExercises = workoutTemplateExerciseRepository.findByWorkoutTemplateId(e.getId());
            return GetWorkoutTemplateDtoMapper.mapToDTO(e, workoutTemplateExercises);
        }).collect(Collectors.toList());
    }

    @Transactional
    public GetWorkoutTemplateDTO getWorkout(Long id, UserDetails userDetails) {
        WorkoutTemplateEntity workout = workoutTemplateRepository.findByIdAndUser_Email(id, userDetails.getUsername())
                .orElseThrow(()-> new ResourceNotFoundException("Workout not found with id " + id));
        return GetWorkoutTemplateDtoMapper.mapToDTO(workout, workoutTemplateExerciseRepository.findByWorkoutTemplateId(id));
    }

    @Transactional
    public void createWorkout(PostWorkoutTemplateDTO workoutDTO, UserDetails userDetails) {
        WorkoutTemplateEntity workoutTemplate = PostWorkoutTemplateDtoMapper.mapFromDTO(workoutDTO);
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("Username not found with email: " + userDetails.getUsername()));
        List<WorkoutTemplateExerciseEntity> workoutExerciseEntities = new ArrayList<>();
        for (PostWorkoutTemplateDTO.ExerciseDTO exerciseDTO: workoutDTO.getExercises()){
            ExerciseEntity exercise = exerciseRepository.findById(exerciseDTO.getId())
                    .orElseThrow(()-> new ResourceNotFoundException("Exercise not found with id " + exerciseDTO.getId()));
            WorkoutTemplateExerciseEntity workoutTemplateExercise = WorkoutTemplateExerciseEntity.builder()
                    .exerciseTemplate(exercise)
                    .workoutTemplate(workoutTemplate)
                    .exerciseDetail(PostWorkoutTemplateDtoMapper.mapFromDTO(exerciseDTO))
                    .build();
            if (exercise.getWorkoutTemplateExercises() == null){
                exercise.setWorkoutTemplateExercises(new HashSet<>());
            }
            exercise.getWorkoutTemplateExercises().add(workoutTemplateExercise);
            workoutExerciseEntities.add(workoutTemplateExercise);
        }
        user.getWorkoutTemplates().add(workoutTemplate);
        workoutTemplate.setUser(user);
        workoutTemplate.setWorkoutExercises(workoutExerciseEntities.stream().collect(Collectors.toSet()));
        workoutTemplateRepository.save(workoutTemplate);
    }

    public void deleteWorkout(Long id, UserDetails userDetails) {
        WorkoutTemplateEntity workoutTemplate = workoutTemplateRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("WorkoutTemplate not found with id " + id));
        workoutTemplateRepository.deleteById(id);
    }

}
