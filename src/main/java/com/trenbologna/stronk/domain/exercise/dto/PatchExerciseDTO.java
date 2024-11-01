package com.trenbologna.stronk.domain.exercise.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class PatchExerciseDTO {
    private Long id;
    private String name;
    private String bodyPart;
    private String category;
}
