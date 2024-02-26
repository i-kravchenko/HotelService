package com.example.HotelBooking.controller;

import com.example.HotelBooking.dto.ResponseList;
import com.example.HotelBooking.dto.room.RoomRequest;
import com.example.HotelBooking.dto.room.RoomResponse;
import com.example.HotelBooking.dto.room.UpsertRoomRequest;
import com.example.HotelBooking.entity.Room;
import com.example.HotelBooking.mapper.RoomMapper;
import com.example.HotelBooking.service.RoomService;
import com.example.HotelBooking.utils.BeanUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController
{
    private final RoomService service;
    private final RoomMapper mapper;

    @GetMapping
    public ResponseEntity<ResponseList<RoomResponse>> findAll(@RequestBody @Valid RoomRequest request) {
        ResponseList<RoomResponse> response = new ResponseList<>();
        Page<Room> page = service.findAll(request);
        response.setItems(page.getContent().stream().map(mapper::roomToResponse).toList());
        response.setTotalCount(page.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{room}")
    public ResponseEntity<RoomResponse> findById(@PathVariable(required = false) Room room) {
        if(room == null) {
            throw new EntityNotFoundException("Room not found");
        }
        return ResponseEntity.ok(mapper.roomToResponse(room));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> create(@RequestBody @Valid UpsertRoomRequest request) {
        Room room = mapper.requestToRoom(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.roomToResponse(service.save(room)));
    }

    @PutMapping("/{room}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> update(@PathVariable(required = false) Room room, @RequestBody UpsertRoomRequest request) {
        if(room == null) {
            throw new EntityNotFoundException("Room not found");
        }
        BeanUtils.copyNonNullProperties(
                mapper.requestToRoom(request),
                room
        );
        return ResponseEntity.ok(mapper.roomToResponse(room));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
