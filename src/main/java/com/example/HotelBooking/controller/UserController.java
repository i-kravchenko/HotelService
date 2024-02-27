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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@Slf4j
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
        log.info("method POST /api/v1/users was called");
        User user = mapper.requestToUser(request);
        user.setRoles(Collections.singleton(role));
        log.info("method POST /api/v1/users returned the response");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.userToResponse(service.register(user)));
    }

    @GetMapping("/{user}")
    public ResponseEntity<UserResponse> findById(@PathVariable(required = false) User user) {
        log.info("method GET /api/v1/users/{id} was called");
        if(user == null) {
            throw new EntityNotFoundException("User not found");
        }
        log.info("method GET /api/v1/users/{} returned the response", user.getId());
        return ResponseEntity.ok(mapper.userToResponse(user));
    }

    @PutMapping("/{user}")
    public ResponseEntity<UserResponse> update(
            @PathVariable(required = false) User user,
            @RequestBody UpsertUserRequest request
    ) {
        log.info("method PUT /api/v1/users/{id} was called");
        if(user == null) {
            throw new EntityNotFoundException("User not found");
        }
        BeanUtils.copyNonNullProperties(
                mapper.requestToUser(request),
                user
        );
        log.info("method PUT /api/v1/users/{} returned the response", user.getId());
        return ResponseEntity.ok(mapper.userToResponse(service.save(user)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("method DELETE /api/v1/users/{} was called", id);
        service.delete(id);
        log.info("method DELETE /api/v1/users/{} returned the response", id);
        return ResponseEntity.noContent().build();
    }
}
