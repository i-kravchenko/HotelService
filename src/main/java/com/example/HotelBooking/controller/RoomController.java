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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController
{
    private final RoomService service;
    private final RoomMapper mapper;

    @GetMapping
    public ResponseEntity<ResponseList<RoomResponse>> findAll(@RequestBody @Valid RoomRequest request) {
        log.info("method GET /api/v1/rooms was called");
        ResponseList<RoomResponse> response = new ResponseList<>();
        Page<Room> page = service.findAll(request);
        response.setItems(page.getContent().stream().map(mapper::roomToResponse).toList());
        response.setTotalCount(page.getTotalElements());
        log.info("method GET /api/v1/rooms returned the response");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{room}")
    public ResponseEntity<RoomResponse> findById(@PathVariable(required = false) Room room) {
        log.info("method GET /api/v1/rooms/{id} was called");
        if(room == null) {
            throw new EntityNotFoundException("Room not found");
        }
        log.info("method GET /api/v1/rooms/{} returned the response", room.getId());
        return ResponseEntity.ok(mapper.roomToResponse(room));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> create(@RequestBody @Valid UpsertRoomRequest request) {
        log.info("method POST /api/v1/rooms was called");
        Room room = mapper.requestToRoom(request);
        log.info("method POST /api/v1/rooms returned the response");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.roomToResponse(service.save(room)));
    }

    @PutMapping("/{room}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> update(@PathVariable(required = false) Room room, @RequestBody UpsertRoomRequest request) {
        log.info("method PUT /api/v1/rooms/{id} was called");
        if(room == null) {
            throw new EntityNotFoundException("Room not found");
        }
        log.info("method PUT /api/v1/rooms/{} was called", room.getId());
        BeanUtils.copyNonNullProperties(
                mapper.requestToRoom(request),
                room
        );
        return ResponseEntity.ok(mapper.roomToResponse(room));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("method DELETE /api/v1/rooms/{} was called", id);
        service.delete(id);
        log.info("method DELETE /api/v1/rooms/{} was called", id);
        return ResponseEntity.noContent().build();
    }
}
