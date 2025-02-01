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
public class UserRegistrationDTO {
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @NotBlank(message = "First name is mandatory")
    private String firstName;
    private String middleName;
    @NotBlank(message = "Last name is mandatory")
    private String lastName;
}
