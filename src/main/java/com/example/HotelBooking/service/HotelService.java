package com.example.HotelBooking.service;

import com.example.HotelBooking.configuration.properties.AppCacheProperties;
import com.example.HotelBooking.dto.ResponseList;
import com.example.HotelBooking.dto.hotel.HotelRequest;
import com.example.HotelBooking.dto.hotel.HotelResponse;
import com.example.HotelBooking.dto.hotel.UpsertHotelRequest;
import com.example.HotelBooking.entity.Hotel;
import com.example.HotelBooking.mapper.HotelMapper;
import com.example.HotelBooking.repository.HotelRepository;
import com.example.HotelBooking.repository.HotelSpecification;
import com.example.HotelBooking.utils.BeanUtils;
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
    private final HotelMapper mapper;

    @Cacheable(value = AppCacheProperties.CacheNames.HOTELS, key = "#request.getPageSize + #request.getPageNumber")
    public ResponseList<HotelResponse> findAll(HotelRequest request) {
        Page<Hotel> page = repository.findAll(
                HotelSpecification.withRequest(request),
                PageRequest.of(request.getPageNumber(), request.getPageSize())
        );
        ResponseList<HotelResponse> response = new ResponseList<>();
        response.setItems(page.getContent().stream().map(this::hotelToResponse).toList());
        response.setTotalCount(page.getTotalElements());
        return response;
    }

    @Cacheable(value = AppCacheProperties.CacheNames.HOTELS, key = "#id")
    public Hotel findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
    }

    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.HOTELS, allEntries = true)
    public HotelResponse add(UpsertHotelRequest request) {
        Hotel hotel = mapper.requestToHotel(request);
        hotel.setRating(0.0);
        hotel.setNumberOfRating(0);
        return hotelToResponse(repository.save(hotel));
    }

    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.HOTELS, allEntries = true)
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.HOTELS, key = "#hotel.getId")
    public HotelResponse hotelRate(Hotel hotel, Integer newMark) {
        int numberOfRating = hotel.getNumberOfRating() + 1;
        double totalRating = hotel.getRating() * numberOfRating;
        totalRating = totalRating - hotel.getRating() + newMark;
        double rating = (double) Math.round(totalRating / numberOfRating * 10) / 10;
        hotel.setRating(rating);
        hotel.setNumberOfRating(numberOfRating);
        return hotelToResponse(repository.save(hotel));
    }

    public HotelResponse hotelToResponse(Hotel hotel) {
        return mapper.hotelToResponse(hotel);
    }

    public HotelResponse update(Hotel hotel, UpsertHotelRequest request) {
        BeanUtils.copyNonNullProperties(
                mapper.requestToHotel(request),
                hotel
        );
        return hotelToResponse(repository.save(hotel));
    }
}
