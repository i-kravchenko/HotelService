package com.example.HotelBooking.controller;

import com.example.HotelBooking.dto.ResponseList;
import com.example.HotelBooking.dto.hotel.HotelRequest;
import com.example.HotelBooking.dto.hotel.HotelResponse;
import com.example.HotelBooking.dto.hotel.UpsertHotelRequest;
import com.example.HotelBooking.entity.Hotel;
import com.example.HotelBooking.mapper.HotelMapper;
import com.example.HotelBooking.service.HotelService;
import com.example.HotelBooking.utils.BeanUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelController
{
    private final HotelService service;
    private final HotelMapper mapper;

    @GetMapping
    public ResponseEntity<ResponseList<HotelResponse>> findAll(@RequestBody @Valid HotelRequest request) {
        ResponseList<HotelResponse> response = new ResponseList<>();
        Page<Hotel> page = service.findAll(request);
        response.setItems(page.getContent().stream().map(mapper::hotelToResponse).toList());
        response.setTotalCount(page.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{hotel}")
    public ResponseEntity<HotelResponse> findById(@PathVariable(required = false) Hotel hotel) {
        if(hotel == null) {
            throw new EntityNotFoundException("Hotel not found");
        }
        return ResponseEntity.ok(mapper.hotelToResponse(hotel));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> create(@RequestBody @Valid UpsertHotelRequest request) {
        Hotel hotel = mapper.requestToHotel(request);
        hotel.setRating(0.0);
        hotel.setNumberOfRating(0);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.hotelToResponse(service.save(hotel)));
    }

    @PutMapping("/{hotel}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> update(@PathVariable(required = false) Hotel hotel, @RequestBody UpsertHotelRequest request) {
        if(hotel == null) {
            throw new EntityNotFoundException("Hotel not found");
        }
        BeanUtils.copyNonNullProperties(
                mapper.requestToHotel(request),
                hotel
        );
        return ResponseEntity.ok(mapper.hotelToResponse(service.save(hotel)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/rating/{hotel}")
    public ResponseEntity<HotelResponse> hotelRate(
            @PathVariable(required = false) Hotel hotel,
            @RequestParam @Min(1) @Max(5) Integer rating
    ) {
        if(hotel == null) {
            throw new EntityNotFoundException("Hotel not found");
        }
        return ResponseEntity.ok()
                .body(mapper.hotelToResponse(service.hotelRate(hotel, rating)));
    }
}
