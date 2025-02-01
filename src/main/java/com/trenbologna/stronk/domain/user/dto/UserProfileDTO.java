package com.trenbologna.stronk.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class UserProfileDTO {
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
}
