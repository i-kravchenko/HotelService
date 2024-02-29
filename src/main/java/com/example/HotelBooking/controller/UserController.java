package com.example.HotelBooking.controller;

import com.example.HotelBooking.dto.user.UpsertUserRequest;
import com.example.HotelBooking.dto.user.UserResponse;
import com.example.HotelBooking.entity.Role;
import com.example.HotelBooking.entity.User;
import com.example.HotelBooking.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public UserResponse register(
            @RequestBody @Valid UpsertUserRequest request,
            @RequestParam Role role,
            HttpServletResponse response
    ) {
        log.info("method POST /api/v1/users was called");
        UserResponse userResponse = service.register(request, role);
        response.setStatus(HttpServletResponse.SC_CREATED);
        log.info("method POST /api/v1/users returned the response");
        return userResponse;
    }

    @GetMapping("/{user}")
    public UserResponse findById(@PathVariable(required = false) User user) {
        log.info("method GET /api/v1/users/{id} was called");
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        log.info("method GET /api/v1/users/{} returned the response", user.getId());
        return service.userToResponse(user);
    }

    @PutMapping("/{user}")
    public UserResponse update(
            @PathVariable(required = false) User user,
            @RequestBody UpsertUserRequest request
    ) {
        log.info("method PUT /api/v1/users/{id} was called");
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        UserResponse response = service.update(user, request);
        log.info("method PUT /api/v1/users/{} returned the response", user.getId());
        return response;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, HttpServletResponse response) {
        log.info("method DELETE /api/v1/users/{} was called", id);
        service.delete(id);
        log.info("method DELETE /api/v1/users/{} returned the response", id);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
