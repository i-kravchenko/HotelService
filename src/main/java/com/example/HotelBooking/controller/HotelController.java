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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelController
{
    private final HotelService service;
    private final HotelMapper mapper;

    @GetMapping
    public ResponseEntity<ResponseList<HotelResponse>> findAll(@RequestBody @Valid HotelRequest request) {
        log.info("method GET /api/v1/hotels was called");
        ResponseList<HotelResponse> response = new ResponseList<>();
        Page<Hotel> page = service.findAll(request);
        response.setItems(page.getContent().stream().map(mapper::hotelToResponse).toList());
        response.setTotalCount(page.getTotalElements());
        log.info("method GET /api/v1/hotels returned the response");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{hotel}")
    public ResponseEntity<HotelResponse> findById(@PathVariable(required = false) Hotel hotel) {
        log.info("method GET /api/v1/hotels/{id} was called");
        if(hotel == null) {
            throw new EntityNotFoundException("Hotel not found");
        }
        log.info("method GET /api/v1/hotels/{} returned the response", hotel.getId());
        return ResponseEntity.ok(mapper.hotelToResponse(hotel));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> create(@RequestBody @Valid UpsertHotelRequest request) {
        log.info("method POST /api/v1/hotels was called");
        Hotel hotel = mapper.requestToHotel(request);
        hotel.setRating(0.0);
        hotel.setNumberOfRating(0);
        log.info("method POST /api/v1/hotels returned the response");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.hotelToResponse(service.save(hotel)));
    }

    @PutMapping("/{hotel}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> update(@PathVariable(required = false) Hotel hotel, @RequestBody UpsertHotelRequest request) {
        log.info("method PUT /api/v1/hotels/{id} was called");
        if(hotel == null) {
            throw new EntityNotFoundException("Hotel not found");
        }
        BeanUtils.copyNonNullProperties(
                mapper.requestToHotel(request),
                hotel
        );
        log.info("method PUT /api/v1/hotels/{} returned the response", hotel.getId());
        return ResponseEntity.ok(mapper.hotelToResponse(service.save(hotel)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("method DELETE /api/v1/hotels/{} was called", id);
        service.delete(id);
        log.info("method DELETE /api/v1/hotels/{} returned the response", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/rating/{hotel}")
    public ResponseEntity<HotelResponse> hotelRate(
            @PathVariable(required = false) Hotel hotel,
            @RequestParam @Min(1) @Max(5) Integer rating
    ) {
        log.info("method PUT /api/v1/hotels/rating/{id} was called");
        if(hotel == null) {
            throw new EntityNotFoundException("Hotel not found");
        }
        log.info("method PUT /api/v1/hotels/rating/{} returned the response", hotel.getId());
        return ResponseEntity.ok()
                .body(mapper.hotelToResponse(service.hotelRate(hotel, rating)));
    }
}
