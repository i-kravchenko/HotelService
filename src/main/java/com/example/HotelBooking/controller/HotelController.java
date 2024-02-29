package com.example.HotelBooking.controller;

import com.example.HotelBooking.dto.ResponseList;
import com.example.HotelBooking.dto.hotel.HotelRequest;
import com.example.HotelBooking.dto.hotel.HotelResponse;
import com.example.HotelBooking.dto.hotel.UpsertHotelRequest;
import com.example.HotelBooking.entity.Hotel;
import com.example.HotelBooking.service.HotelService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService service;

    @GetMapping
    public ResponseList<HotelResponse> findAll(@RequestBody @Valid HotelRequest request) {
        log.info("method GET /api/v1/hotels was called");
        ResponseList<HotelResponse> response = service.findAll(request);
        log.info("method GET /api/v1/hotels returned the response");
        return response;
    }

    @GetMapping("/{hotel}")
    public HotelResponse findById(@PathVariable(required = false) Hotel hotel) {
        log.info("method GET /api/v1/hotels/{id} was called");
        if (hotel == null) {
            throw new EntityNotFoundException("Hotel not found");
        }
        log.info("method GET /api/v1/hotels/{} returned the response", hotel.getId());
        return service.hotelToResponse(hotel);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HotelResponse create(
            @RequestBody @Valid UpsertHotelRequest request,
            HttpServletResponse response
    ) {
        log.info("method POST /api/v1/hotels was called");
        HotelResponse hotelResponse = service.add(request);
        log.info("method POST /api/v1/hotels returned the response");
        response.setStatus(HttpServletResponse.SC_CREATED);
        return hotelResponse;
    }

    @PutMapping("/{hotel}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HotelResponse update(
            @PathVariable(required = false) Hotel hotel,
            @RequestBody UpsertHotelRequest request
    ) {
        log.info("method PUT /api/v1/hotels/{id} was called");
        if (hotel == null) {
            throw new EntityNotFoundException("Hotel not found");
        }
        HotelResponse response = service.update(hotel, request);
        log.info("method PUT /api/v1/hotels/{} returned the response", hotel.getId());
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable Long id, HttpServletResponse response) {
        log.info("method DELETE /api/v1/hotels/{} was called", id);
        service.delete(id);
        log.info("method DELETE /api/v1/hotels/{} returned the response", id);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @PutMapping("/rating/{hotel}")
    public HotelResponse hotelRate(
            @PathVariable(required = false) Hotel hotel,
            @RequestParam @Min(1) @Max(5) Integer rating
    ) {
        log.info("method PUT /api/v1/hotels/rating/{id} was called");
        if (hotel == null) {
            throw new EntityNotFoundException("Hotel not found");
        }
        log.info("method PUT /api/v1/hotels/rating/{} returned the response", hotel.getId());
        return service.hotelRate(hotel, rating);
    }
}
