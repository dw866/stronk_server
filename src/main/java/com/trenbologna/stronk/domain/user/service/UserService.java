package com.trenbologna.stronk.domain.user.service;

import com.trenbologna.stronk.domain.user.UserEntity;
import com.trenbologna.stronk.domain.user.auth.JwtService;
import com.trenbologna.stronk.domain.user.dto.UserLoginDTO;
import com.trenbologna.stronk.domain.user.dto.UserProfileDTO;
import com.trenbologna.stronk.domain.user.dto.UserRegistrationDTO;
import com.trenbologna.stronk.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtService jwtService;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public UserEntity register(UserRegistrationDTO userDTO){
        /**TODO: Passwords must be:
         * - At least 12 characters
         * - Contain no space
         * - At least 1 number, special character, and letters
         * **/
        UserEntity user = UserEntity.builder()
                .email(userDTO.getEmail())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .middleName(userDTO.getMiddleName())
                .password(encoder.encode(userDTO.getPassword()))
                .build();
        return userRepository.save(user);
    }
    public String verify(UserLoginDTO userDTO){
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
        );
        if (authentication.isAuthenticated()) return jwtService.generateToken(userDTO.getEmail());
        return "fail";
    }
    public UserProfileDTO getProfile(UserDetails userDetails){
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("Username not found with email: " + userDetails.getUsername()));
        return UserProfileDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .build();

    }
    public void logout(String jwtToken){
        jwtService.blacklistToken(jwtToken);
    }
}
