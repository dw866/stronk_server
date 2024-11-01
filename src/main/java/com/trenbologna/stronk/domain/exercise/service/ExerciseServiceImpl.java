package com.trenbologna.stronk.domain.exercise.service;

import com.trenbologna.stronk.domain.exercise.ExerciseEntity;
import com.trenbologna.stronk.domain.exercise.dto.GetExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PostExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PatchExerciseDTO;
import com.trenbologna.stronk.domain.exercise.mapper.GetExerciseDtoMapper;
import com.trenbologna.stronk.domain.exercise.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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
    public List<GetExerciseDTO> getAllExercises() {
        //todo this should eventually filter by user
        return GetExerciseDtoMapper.mapToDTO(exerciseRepository.findAll());
    }

    @Override
    public GetExerciseDTO getExercise(Long id) {
        return GetExerciseDtoMapper.mapToDTO(exerciseRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Exercise not found with id " + id)));
    }

    @Override
    @Transactional
    public void createExercise(PostExerciseDTO postExerciseDTO) {
        ExerciseEntity exercise = ExerciseEntity.builder()
                .name(postExerciseDTO.getName())
                .category(postExerciseDTO.getCategory())
                .bodyPart(postExerciseDTO.getBodyPart())
                .build();
        exerciseRepository.save(exercise);
    }
    @Override
    @Transactional
    public void patchExercise(Long id, PatchExerciseDTO putExerciseDTO) {
        ExerciseEntity exercise = exerciseRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Exercise not found with id " + id))
                .setName(putExerciseDTO.getName())
                .setBodyPart(putExerciseDTO.getBodyPart())
                .setCategory(putExerciseDTO.getCategory());
        exerciseRepository.save(exercise);

    }


    @Override
    public void deleteExercise(Long id) {
        exerciseRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Exercise not found with id " + id));
        exerciseRepository.deleteById(id);
    }

}
