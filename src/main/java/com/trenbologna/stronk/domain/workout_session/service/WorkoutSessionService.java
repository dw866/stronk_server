package com.trenbologna.stronk.domain.workout_session.service;

import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.user.UserEntity;
import com.trenbologna.stronk.domain.user.repository.UserRepository;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class WorkoutSessionService {
    private final WorkoutSessionRepository workoutSessionRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    @Autowired
    public WorkoutSessionService(WorkoutSessionRepository workoutSessionRepository,
                                 ExerciseRepository exerciseRepository,
                                 UserRepository userRepository){
        this.workoutSessionRepository = workoutSessionRepository;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
    }
//    public GetWorkoutSessionDTO getWorkoutSession(Long id){
//        WorkoutSessionEntity workoutSession = workoutSessionRepository.findById(id)
//                .orElseThrow(()-> new ResourceNotFoundException("Workout not found with id " + id));
//        return GetWorkoutSessionDtoMapper.mapToDTO(workoutSession);
//    }

    public List<GetWorkoutSessionDTO> getWorkoutSessions(UserDetails userDetails){
        List<WorkoutSessionEntity> workoutSessions = workoutSessionRepository.findByUser_Email(userDetails.getUsername());
        return GetWorkoutSessionDtoMapper.mapToDTO(workoutSessions);

    }

    @Transactional
    public void performWorkoutSession(PostWorkoutSessionDTO postWorkoutSessionDTO, UserDetails userDetails) {
        WorkoutSessionEntity workoutSession = GetWorkoutSessionDtoMapper.mapFromDTO(postWorkoutSessionDTO);
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("Username not found with email: " + userDetails.getUsername()));
        Set<WorkoutSessionExerciseEntity> workoutSessionExercises = new HashSet<>();
        for (PostWorkoutSessionDTO.ExerciseDTO exerciseDTO : postWorkoutSessionDTO.getExercises()){
            ExerciseEntity exercise = exerciseRepository.findById(exerciseDTO.getId())
                    .orElseThrow(()-> new ResourceNotFoundException("Exercise not found with id " + exerciseDTO.getId()));
            WorkoutSessionExerciseEntity workoutSessionExercise = WorkoutSessionExerciseEntity.builder()
                    .workoutSession(workoutSession)
                    .exerciseSession(exercise)
                    .exerciseSessionDetail(PostWorkoutSessionDtoMapper.mapFromDTO(exerciseDTO))
                    .build();
            if (exercise.getWorkoutSessionExercises() == null){
                exercise.setWorkoutSessionExercises(new HashSet<>());
            }
            exercise.getWorkoutSessionExercises().add(workoutSessionExercise);
            workoutSessionExercises.add(workoutSessionExercise);
        }
        user.getWorkoutSessions().add(workoutSession);
        workoutSession.setUser(user);
        workoutSession.setWorkoutSessionExercises(workoutSessionExercises);
        workoutSessionRepository.save(workoutSession);

    }
}
