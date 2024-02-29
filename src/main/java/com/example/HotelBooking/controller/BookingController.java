package com.example.HotelBooking.controller;

import com.example.HotelBooking.dto.Request;
import com.example.HotelBooking.dto.ResponseList;
import com.example.HotelBooking.dto.booking.BookingResponse;
import com.example.HotelBooking.dto.booking.UpsertBookingRequest;
import com.example.HotelBooking.service.BookingService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController
{
    private final BookingService service;

    @PostMapping
    public BookingResponse book(
            @RequestBody @Valid UpsertBookingRequest request,
            HttpServletResponse response
    ) {
        log.info("method POST /api/v1/booking was called");
        BookingResponse bookingResponse = service.bookRoom(request);
        log.info("method POST /api/v1/booking returned the response");
        response.setStatus(HttpServletResponse.SC_CREATED);
        return bookingResponse;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseList<BookingResponse> findAll(@RequestBody @Valid Request request) {
        log.info("method GET /api/v1/booking was called");
        ResponseList<BookingResponse> response = service.findAll(request);
        log.info("method GET /api/v1/booking returned the response");
        return response;
    }
}
