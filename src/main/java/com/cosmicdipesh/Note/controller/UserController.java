package com.cosmicdipesh.Note.controller;

import com.cosmicdipesh.Note.entity.ApiResponse;
import com.cosmicdipesh.Note.entity.AuthenticationResponse;
import com.cosmicdipesh.Note.entity.User;
import com.cosmicdipesh.Note.service.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addUser(@RequestBody User user) {
      AuthenticationResponse response = userService.register(user);
      return new ResponseEntity<>(new ApiResponse<>("User registered successfully", response.getToken(), HttpStatus.OK.value()),
              HttpStatus.OK);
    }


}
