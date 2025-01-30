package com.example.service_user.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.base_domain.entities.UserDetail;
import com.example.service_user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/admin/users")
    public ResponseEntity<Object> getUsers() throws Exception {
        return userService.getUsers();
    }

    @GetMapping("/admin/user")
    public ResponseEntity<Object> getUser(@RequestBody UUID id) throws Exception {
        return userService.getUser(id);
    }

    // @PutMapping("/admin/user")
    // public ResponseEntity<Object> updateUser(@RequestBody ) throws Exception {
    // return userService.updateUser(user);
    // }

    @DeleteMapping("/admin/user")
    public ResponseEntity<Object> deleteUser(@RequestBody UUID id) throws Exception {
        return userService.deleteUser(id);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<Object> getProfile() throws Exception {
        return userService.getProfile();
    }

    @PutMapping("/user/profile")
    public ResponseEntity<Object> updateProfile(@RequestBody UserDetail profile) throws Exception {
        return userService.updateProfile(profile);
    }

    @DeleteMapping("/user/profile")
    public ResponseEntity<Object> deleteProfile() throws Exception {
        return userService.deleteProfile();
    }
}
