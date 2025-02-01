package com.trenbologna.stronk.domain.user.auth;

import com.trenbologna.stronk.domain.user.UserEntity;
import com.trenbologna.stronk.domain.user.auth.UserPrinciple;
import com.trenbologna.stronk.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
        return new UserPrinciple(user);
    }
}
