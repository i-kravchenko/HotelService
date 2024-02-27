package com.example.HotelBooking.service;

import com.example.HotelBooking.configuration.properties.AppCacheProperties;
import com.example.HotelBooking.dto.room.RoomRequest;
import com.example.HotelBooking.entity.Room;
import com.example.HotelBooking.repository.RoomRepository;
import com.example.HotelBooking.repository.RoomSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService
{
    private final RoomRepository repository;

    @Cacheable(value = AppCacheProperties.CacheNames.ROOMS, key = "#request.getPageSize + #request.getPageNumber")
    public Page<Room> findAll(RoomRequest request) {
        return repository.findAll(
                RoomSpecification.withRequest(request),
                PageRequest.of(request.getPageNumber(), request.getPageSize())
        );
    }

    @Cacheable(value = AppCacheProperties.CacheNames.ROOMS, key = "#id")
    public Room findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
    }

    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.ROOMS, allEntries = true)
    public Room save(Room room) {
        return repository.save(room);
    }

    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.ROOMS, allEntries = true)
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
