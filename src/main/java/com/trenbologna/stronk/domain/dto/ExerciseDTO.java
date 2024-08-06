package com.trenbologna.stronk.domain.dto;

import com.trenbologna.stronk.domain.WorkoutEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExerciseDTO {
    private Long id;
    private String name;
    private String bodyPart;
    private String category;
}
