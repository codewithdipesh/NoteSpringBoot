package com.cosmicdipesh.Note.service;

import com.cosmicdipesh.Note.ExceptionHandler.ExistingUserException;
import com.cosmicdipesh.Note.ExceptionHandler.ValidationException;
import com.cosmicdipesh.Note.config.SecurityConfig;
import com.cosmicdipesh.Note.entity.AuthenticationResponse;
import com.cosmicdipesh.Note.entity.Role;
import com.cosmicdipesh.Note.entity.User;
import com.cosmicdipesh.Note.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request){
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if(existingUser.isPresent()){
            throw new ExistingUserException("Username is already taken");
        }else{
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.USER);
            User savedUser = userRepository.save(user);
            String token = jwtService.generateToken(savedUser);
            return new AuthenticationResponse(token);
        }

    }

    public AuthenticationResponse login(User request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword())
        );
       Optional<User> user = userRepository.findByUsername(request.getUsername());
       if(user.isPresent()){
           String token = jwtService.generateToken(user.get());
           return new AuthenticationResponse(token);
       }
       throw new ValidationException("User doesnt exists");

    }

    public User updateUser(String username ,User request){

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public boolean deleteUser(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(user);
        return true;
    }
}
