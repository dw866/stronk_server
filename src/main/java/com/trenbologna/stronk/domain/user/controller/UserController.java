package com.trenbologna.stronk.domain.user.controller;

import com.trenbologna.stronk.domain.user.dto.UserLoginDTO;
import com.trenbologna.stronk.domain.user.dto.UserProfileDTO;
import com.trenbologna.stronk.domain.user.dto.UserRegistrationDTO;
import com.trenbologna.stronk.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    ResponseEntity<String> register(@Valid @RequestBody UserRegistrationDTO userDTO){
        userService.register(userDTO);
        String jwtToken = userService.verify(
                UserLoginDTO.builder()
                        .email(userDTO.getEmail())
                        .password(userDTO.getPassword())
                .build());
        return ResponseEntity.ok(jwtToken);
    }
    @PostMapping("/login")
    ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userDTO){
        String jwtToken = userService.verify(userDTO);

        return ResponseEntity.ok(jwtToken);
    }
    @PostMapping("/logout")
    ResponseEntity logout(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                          HttpServletRequest request){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
            String jwtToken = authorizationHeader.substring(7);
            userService.logout(jwtToken);
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping("/profile")
    ResponseEntity<UserProfileDTO> profile(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(userService.getProfile(userDetails));
    }
}
