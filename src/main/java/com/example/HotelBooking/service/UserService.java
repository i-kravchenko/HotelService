package com.example.HotelBooking.service;

import com.example.HotelBooking.dto.event.UserRegisterEvent;
import com.example.HotelBooking.dto.user.UpsertUserRequest;
import com.example.HotelBooking.dto.user.UserResponse;
import com.example.HotelBooking.entity.Role;
import com.example.HotelBooking.entity.User;
import com.example.HotelBooking.mapper.UserMapper;
import com.example.HotelBooking.repository.UserRepository;
import com.example.HotelBooking.utils.BeanUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService
{
    @Value("${app.kafka.kafkaMessageUserRegisterTopic}")
    private String topic;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final KafkaTemplate<String, UserRegisterEvent> kafkaTemplate;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public UserResponse register(UpsertUserRequest request, Role role) {
        User user = mapper.requestToUser(request);
        user.setRoles(Collections.singleton(role));
        Example<User> example = Example.of(User.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build());
        if(repository.exists(example)) {
            throw new EntityExistsException("User already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = repository.save(user);
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), mapper.userToEvent(user));
        return userToResponse(user);
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public UserResponse update(User user, UpsertUserRequest request) {
        BeanUtils.copyNonNullProperties(
                mapper.requestToUser(request),
                user
        );
        return userToResponse(save(user));
    }

    public UserResponse userToResponse(User user) {
        return mapper.userToResponse(user);
    }
}
