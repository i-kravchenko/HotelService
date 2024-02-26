package com.example.HotelBooking.controller;

import com.example.HotelBooking.dto.user.UpsertUserRequest;
import com.example.HotelBooking.dto.user.UserResponse;
import com.example.HotelBooking.entity.Role;
import com.example.HotelBooking.entity.User;
import com.example.HotelBooking.mapper.UserMapper;
import com.example.HotelBooking.service.UserService;
import com.example.HotelBooking.utils.BeanUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController
{
    private final UserService service;
    private final UserMapper mapper;

    @PostMapping
    public ResponseEntity<UserResponse> register(
            @RequestBody @Valid UpsertUserRequest request,
            @RequestParam Role role
            ) {
        User user = mapper.requestToUser(request);
        user.setRoles(Collections.singleton(role));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.userToResponse(service.register(user)));
    }

    @GetMapping("/{user}")
    public ResponseEntity<UserResponse> findById(@PathVariable(required = false) User user) {
        if(user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return ResponseEntity.ok(mapper.userToResponse(user));
    }

    @PutMapping("/{user}")
    public ResponseEntity<UserResponse> update(
            @PathVariable(required = false) User user,
            @RequestBody UpsertUserRequest request
    ) {
        if(user == null) {
            throw new EntityNotFoundException("User not found");
        }
        BeanUtils.copyNonNullProperties(
                mapper.requestToUser(request),
                user
        );
        return ResponseEntity.ok(mapper.userToResponse(service.save(user)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
