package com.example.HotelBooking.controller;

import com.example.HotelBooking.dto.Request;
import com.example.HotelBooking.dto.ResponseList;
import com.example.HotelBooking.dto.booking.BookingResponse;
import com.example.HotelBooking.dto.booking.UpsertBookingRequest;
import com.example.HotelBooking.entity.Booking;
import com.example.HotelBooking.mapper.BookingMapper;
import com.example.HotelBooking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController
{
    private final BookingService service;
    private final BookingMapper mapper;

    @PostMapping
    public ResponseEntity<BookingResponse> book(@RequestBody @Valid UpsertBookingRequest request) {
        log.info("method POST /api/v1/booking was called");
        Booking booking = mapper.requestToBooking(request);
        log.info("method POST /api/v1/booking returned the response");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.bookingToResponse(service.bookRoom(booking)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseList<BookingResponse>> findAll(@RequestBody @Valid Request request) {
        log.info("method GET /api/v1/booking was called");
        ResponseList<BookingResponse> response = new ResponseList<>();
        Page<Booking> page = service.findAll(request);
        response.setItems(page.getContent().stream().map(mapper::bookingToResponse).toList());
        response.setTotalCount(page.getTotalElements());
        log.info("method GET /api/v1/booking returned the response");
        return ResponseEntity.ok(response);
    }
}
