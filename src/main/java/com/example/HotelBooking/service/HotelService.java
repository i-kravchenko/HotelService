package com.example.HotelBooking.service;

import com.example.HotelBooking.dto.hotel.HotelRequest;
import com.example.HotelBooking.entity.Hotel;
import com.example.HotelBooking.repository.HotelRepository;
import com.example.HotelBooking.repository.HotelSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelService
{
    private final HotelRepository repository;

    public Page<Hotel> findAll(HotelRequest request) {
        return repository.findAll(
                HotelSpecification.withRequest(request),
                PageRequest.of(request.getPageNumber(), request.getPageSize())
        );
    }

    public Hotel findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
    }

    public Hotel save(Hotel hotel) {
        return repository.save(hotel);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Hotel hotelRate(Hotel hotel, Integer newMark) {
        int numberOfRating = hotel.getNumberOfRating() + 1;
        double totalRating = hotel.getRating() * numberOfRating;
        totalRating = totalRating - hotel.getRating() + newMark;
        double rating = (double) Math.round(totalRating / numberOfRating * 10) / 10;
        hotel.setRating(rating);
        hotel.setNumberOfRating(numberOfRating);
        return repository.save(hotel);
    }
}
