package com.trenbologna.stronk.domain.exercise.service;

import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.exercise.dto.GetExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PostExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PatchExerciseDTO;
import com.trenbologna.stronk.domain.exercise.mapper.GetExerciseDtoMapper;
import com.trenbologna.stronk.domain.exercise.repository.ExerciseRepository;
import com.trenbologna.stronk.domain.user.UserEntity;
import com.trenbologna.stronk.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository,
                           UserRepository userRepository
    ){
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
    }

    public List<GetExerciseDTO> getAllExercises(UserDetails userDetails) {
        return GetExerciseDtoMapper.mapToDTO(exerciseRepository.findExercisesByUser_Email(userDetails.getUsername()));
    }

    @Transactional
    public void createExercise(PostExerciseDTO postExerciseDTO, UserDetails userDetails) {
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("Username not found with email: " + userDetails.getUsername()));
        ExerciseEntity exercise = ExerciseEntity.builder()
                .user(user)
                .name(postExerciseDTO.getName())
                .category(postExerciseDTO.getCategory())
                .bodyPart(postExerciseDTO.getBodyPart())
                .build();
        user.getExercises().add(exercise);
        userRepository.save(user);
    }
    @Transactional
    public void patchExercise(Long id, PatchExerciseDTO putExerciseDTO, UserDetails userDetails) {
        ExerciseEntity exercise = exerciseRepository.findByIdAndUser_Email(id, userDetails.getUsername()).orElseThrow(()->
                new ResourceNotFoundException("Exercise not found with id " + id))
                .setName(putExerciseDTO.getName())
                .setBodyPart(putExerciseDTO.getBodyPart())
                .setCategory(putExerciseDTO.getCategory());

        exerciseRepository.save(exercise);

    }

    @Transactional
    public void deleteExercise(Long id, UserDetails userDetails) {
        ExerciseEntity exercise = exerciseRepository.findByIdAndUser_Email(id, userDetails.getUsername())
                .orElseThrow(()-> new ResourceNotFoundException("Exercise not found with id " + id));
        exerciseRepository.deleteByIdAndUser_Email(id, userDetails.getUsername());
    }

}
