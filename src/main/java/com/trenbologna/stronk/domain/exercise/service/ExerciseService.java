package com.trenbologna.stronk.domain.exercise.service;

import com.trenbologna.stronk.domain.exercise.dto.GetExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PostExerciseDTO;
import com.trenbologna.stronk.domain.exercise.dto.PatchExerciseDTO;

import java.util.List;

public interface ExerciseService {
    public List<GetExerciseDTO> getAllExercises();
    public GetExerciseDTO getExercise(Long id);
    public void createExercise(PostExerciseDTO postExerciseDTO);
    public void patchExercise(Long id, PatchExerciseDTO putExerciseDTO);
    public void deleteExercise(Long id);

}
