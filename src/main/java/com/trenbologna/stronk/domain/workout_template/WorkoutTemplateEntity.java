package com.trenbologna.stronk.domain.workout_template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trenbologna.stronk.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class WorkoutTemplateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "workoutTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<WorkoutTemplateExerciseEntity> workoutExercises;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


}
