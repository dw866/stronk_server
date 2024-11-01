package com.trenbologna.stronk.domain.workout_session.service;

import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.workout_session.WorkoutSessionEntity;
import com.trenbologna.stronk.domain.workout_session.WorkoutSessionExerciseEntity;
import com.trenbologna.stronk.domain.workout_session.dto.GetWorkoutSessionDTO;
import com.trenbologna.stronk.domain.workout_session.dto.PostWorkoutSessionDTO;
import com.trenbologna.stronk.domain.workout_session.mapper.PostWorkoutSessionDtoMapper;
import com.trenbologna.stronk.domain.workout_session.mapper.GetWorkoutSessionDtoMapper;
import com.trenbologna.stronk.domain.workout_session.repository.WorkoutSessionRepository;
import com.trenbologna.stronk.domain.exercise.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class WorkoutSessionServiceImpl implements WorkoutSessionService {
    private final WorkoutSessionRepository workoutSessionRepository;
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public WorkoutSessionServiceImpl(WorkoutSessionRepository workoutSessionRepository, ExerciseRepository exerciseRepository){
        this.workoutSessionRepository = workoutSessionRepository;
        this.exerciseRepository = exerciseRepository;
    }
    @Override
    public GetWorkoutSessionDTO getWorkoutSession(Long id){
        WorkoutSessionEntity workoutSession = workoutSessionRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Workout not found with id " + id));
        return GetWorkoutSessionDtoMapper.mapToDTO(workoutSession);
    }
    @Override
    public List<GetWorkoutSessionDTO> getAllWorkoutSession(){
        List<WorkoutSessionEntity> workoutSessions = workoutSessionRepository.findAll();
        return GetWorkoutSessionDtoMapper.mapToDTO(workoutSessions);

    }
    @Override
    @Transactional
    public void performWorkoutSession(PostWorkoutSessionDTO postWorkoutSessionDTO) {
        WorkoutSessionEntity workoutSession = GetWorkoutSessionDtoMapper.mapFromDTO(postWorkoutSessionDTO);

        Set<WorkoutSessionExerciseEntity> workoutSessionExercises = new HashSet<>();
        for (PostWorkoutSessionDTO.ExerciseDTO exerciseDTO : postWorkoutSessionDTO.getExercises()){
            ExerciseEntity exerciseSession = exerciseRepository.findById(exerciseDTO.getId())
                    .orElseThrow(()-> new ResourceNotFoundException("Exercise not found with id " + exerciseDTO.getId()));
            WorkoutSessionExerciseEntity workoutSessionExercise = WorkoutSessionExerciseEntity.builder()
                    .workoutSession(workoutSession)
                    .exerciseSession(exerciseSession)
                    .exerciseSessionDetail(PostWorkoutSessionDtoMapper.mapFromDTO(exerciseDTO))
                    .build();
            workoutSessionExercises.add(workoutSessionExercise);
        }
        workoutSession.setWorkoutSessionExercises(workoutSessionExercises);
        workoutSessionRepository.save(workoutSession);

    }
}
