package com.example.HotelBooking.service;

import com.example.HotelBooking.configuration.properties.AppCacheProperties;
import com.example.HotelBooking.dto.hotel.HotelRequest;
import com.example.HotelBooking.entity.Hotel;
import com.example.HotelBooking.repository.HotelRepository;
import com.example.HotelBooking.repository.HotelSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheManager = "redisCacheManager")
public class HotelService
{
    private final HotelRepository repository;

    @Cacheable(value = AppCacheProperties.CacheNames.HOTELS, key = "#request.getPageSize + #request.getPageNumber")
    public Page<Hotel> findAll(HotelRequest request) {
        return repository.findAll(
                HotelSpecification.withRequest(request),
                PageRequest.of(request.getPageNumber(), request.getPageSize())
        );
    }

    @Cacheable(value = AppCacheProperties.CacheNames.HOTELS, key = "#id")
    public Hotel findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
    }

    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.HOTELS, allEntries = true)
    public Hotel save(Hotel hotel) {
        return repository.save(hotel);
    }

    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.HOTELS, allEntries = true)
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.HOTELS, key = "#hotel.getId")
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
