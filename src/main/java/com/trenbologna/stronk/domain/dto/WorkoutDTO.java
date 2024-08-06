package com.trenbologna.stronk.domain.dto;

import com.trenbologna.stronk.domain.ExerciseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutDTO {
    private Long id;
    private String name;
    private String note;
    private List<ExerciseEntity> exercises;

}
