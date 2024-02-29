package com.example.HotelBooking.controller;

import com.example.HotelBooking.dto.ResponseList;
import com.example.HotelBooking.dto.room.RoomRequest;
import com.example.HotelBooking.dto.room.RoomResponse;
import com.example.HotelBooking.dto.room.UpsertRoomRequest;
import com.example.HotelBooking.entity.Room;
import com.example.HotelBooking.service.RoomService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService service;

    @GetMapping
    public ResponseList<RoomResponse> findAll(@RequestBody @Valid RoomRequest request) {
        log.info("method GET /api/v1/rooms was called");
        ResponseList<RoomResponse> response = service.findAll(request);
        log.info("method GET /api/v1/rooms returned the response");
        return response;
    }

    @GetMapping("/{room}")
    public RoomResponse findById(@PathVariable(required = false) Room room) {
        log.info("method GET /api/v1/rooms/{id} was called");
        if (room == null) {
            throw new EntityNotFoundException("Room not found");
        }
        log.info("method GET /api/v1/rooms/{} returned the response", room.getId());
        return service.roomToResponse(room);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RoomResponse create(
            @RequestBody @Valid UpsertRoomRequest request,
            HttpServletResponse response
    ) {
        log.info("method POST /api/v1/rooms was called");
        RoomResponse roomResponse = service.add(request);
        log.info("method POST /api/v1/rooms returned the response");
        response.setStatus(HttpServletResponse.SC_CREATED);
        return roomResponse;
    }

    @PutMapping("/{room}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RoomResponse update(@PathVariable(required = false) Room room, @RequestBody UpsertRoomRequest request) {
        log.info("method PUT /api/v1/rooms/{id} was called");
        if (room == null) {
            throw new EntityNotFoundException("Room not found");
        }
        RoomResponse response = service.update(room, request);
        log.info("method PUT /api/v1/rooms/{} returned the response", room.getId());
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable Long id, HttpServletResponse response) {
        log.info("method DELETE /api/v1/rooms/{} was called", id);
        service.delete(id);
        log.info("method DELETE /api/v1/rooms/{} was called", id);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
