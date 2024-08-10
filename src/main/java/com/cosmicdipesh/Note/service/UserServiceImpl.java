package com.cosmicdipesh.Note.service;

import com.cosmicdipesh.Note.entity.User;
import com.cosmicdipesh.Note.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
    return userRepository.findByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException("User not found with username: " + username)
    );
    }
}
