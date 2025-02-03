package com.example.service_user.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.base_domain.dtos.UserDetailDTO;
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
    public ResponseEntity<Object> getUser(@RequestBody UUID id) throws Exception {
        return userService.getUser(id);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUser(@RequestBody UserDetailDTO userDetailDTO) throws Exception {
        return userService.updateUser(userDetailDTO);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteUser(@RequestBody UUID id) throws Exception {
        return userService.deleteUser(id);
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
}
