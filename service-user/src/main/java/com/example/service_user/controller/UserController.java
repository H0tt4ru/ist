package com.example.service_user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.base_domain.dtos.UserDetailDTO;
import com.example.service_user.request.GetTopRequest;
import com.example.service_user.request.GetUserRequest;
import com.example.service_user.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-detail")
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody UserDetailDTO userDetailDTO) throws Exception {
        return userService.createUser(userDetailDTO);
    }

    @GetMapping("/get-all")
    public ResponseEntity<Object> getUsers() throws Exception {
        return userService.getUsers();
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getUser(@RequestBody GetUserRequest userRequest) throws Exception {
        return userService.getUser(userRequest);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUser(@RequestBody UserDetailDTO userDetailDTO)
            throws Exception {
        return userService.updateUser(userDetailDTO);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteUser(@RequestBody GetUserRequest userGetRequest) throws Exception {
        return userService.deleteUser(userGetRequest);
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile() throws Exception {
        return userService.getProfile();
    }

    @PutMapping("/update-profile")
    public ResponseEntity<Object> updateProfile(@RequestBody UserDetailDTO userDetailDTO) throws Exception {
        return userService.updateProfile(userDetailDTO);
    }

    @DeleteMapping("/delete-profile")
    public ResponseEntity<Object> deleteProfile() throws Exception {
        return userService.deleteProfile();
    }

    @GetMapping("/get-top")
    public ResponseEntity<Object> getTopUsers(@RequestBody GetTopRequest topRequest) throws Exception {
        return userService.getTopUsers(topRequest);
    }
}
