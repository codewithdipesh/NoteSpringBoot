package com.cosmicdipesh.Note.controller;

import com.cosmicdipesh.Note.ExceptionHandler.ValidationException;
import com.cosmicdipesh.Note.entity.ApiResponse;
import com.cosmicdipesh.Note.entity.AuthenticationResponse;
import com.cosmicdipesh.Note.entity.User;
import com.cosmicdipesh.Note.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public")
public class publicController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-new")
    public ResponseEntity<ApiResponse<String>> addUser(@RequestBody User user) {
        if(user.getUsername() == null || user.getPassword() == null || user.getUsername().isEmpty() || user.getPassword().isEmpty() ){
            throw new ValidationException("Username and password are required");
        }
        AuthenticationResponse response = userService.register(user);
        return new ResponseEntity<>(new ApiResponse<>("User registered successfully", response.getToken(), HttpStatus.OK.value()),
                HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> loginUser(@RequestBody User user) {
        if(user.getUsername() == null || user.getPassword() == null || user.getUsername().isEmpty() || user.getPassword().isEmpty() ){
            throw new ValidationException("Username and password are required");
        }
        AuthenticationResponse response = userService.login(user);
        return new ResponseEntity<>(new ApiResponse<>("User loggedIn successfully", response.getToken(), HttpStatus.OK.value()),
                HttpStatus.OK);
    }


    @GetMapping("/")
    public ResponseEntity<String> healthcheck() {
        return new ResponseEntity<>(
                "Server is up and running",
                HttpStatus.OK);
    }
}
