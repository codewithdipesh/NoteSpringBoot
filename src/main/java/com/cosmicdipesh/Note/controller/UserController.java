package com.cosmicdipesh.Note.controller;

import com.cosmicdipesh.Note.ExceptionHandler.UnauthorizedException;
import com.cosmicdipesh.Note.ExceptionHandler.ValidationException;
import com.cosmicdipesh.Note.entity.ApiResponse;
import com.cosmicdipesh.Note.entity.AuthenticationResponse;
import com.cosmicdipesh.Note.entity.ChangePasswordRequest;
import com.cosmicdipesh.Note.entity.User;
import com.cosmicdipesh.Note.service.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Boolean>> updatepassword(@RequestBody ChangePasswordRequest request) {

        String password = request.getNewPassword();
//        System.out.println(password);
       if(password == null){
           throw new ValidationException("Password cannot be null");
       }else{
           String username = SecurityContextHolder.getContext().getAuthentication().getName();
           //new user with changed password
           User user = new User();
           user.setUsername(username);
           user.setPassword(password);

           User response = userService.updateUser(username,user);
//           System.out.println(response);
           return new ResponseEntity<>(new ApiResponse<>("Updated successfully",true, HttpStatus.OK.value()),
                   HttpStatus.OK);
       }

    }

    @DeleteMapping("/delete_account")
    public ResponseEntity<ApiResponse<Boolean>> deleteUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.deleteUser(username);
        return new ResponseEntity<>(new ApiResponse<>("User deleted Successfully",true,HttpStatus.OK.value()),
        HttpStatus.OK);
    }

    @GetMapping("/authenticate")
    public ResponseEntity<ApiResponse<Boolean>> authenticate(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username == null){
            throw new UnauthorizedException("UnAuthenticated User");
        }
        return new ResponseEntity<>(new ApiResponse<>("authorized",true,HttpStatus.OK.value()),
                HttpStatus.OK);
    }



}
