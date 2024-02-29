package com.example.HotelBooking.service;

import com.example.HotelBooking.configuration.properties.AppCacheProperties;
import com.example.HotelBooking.dto.ResponseList;
import com.example.HotelBooking.dto.room.RoomRequest;
import com.example.HotelBooking.dto.room.RoomResponse;
import com.example.HotelBooking.dto.room.UpsertRoomRequest;
import com.example.HotelBooking.entity.Room;
import com.example.HotelBooking.mapper.RoomMapper;
import com.example.HotelBooking.repository.RoomRepository;
import com.example.HotelBooking.repository.RoomSpecification;
import com.example.HotelBooking.utils.BeanUtils;
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
    private final RoomMapper mapper;

    @Cacheable(value = AppCacheProperties.CacheNames.ROOMS, key = "#request.getPageSize + #request.getPageNumber")
    public ResponseList<RoomResponse> findAll(RoomRequest request) {
        Page<Room> page = repository.findAll(
                RoomSpecification.withRequest(request),
                PageRequest.of(request.getPageNumber(), request.getPageSize())
        );
        ResponseList<RoomResponse> response = new ResponseList<>();
        response.setItems(page.getContent().stream().map(this::roomToResponse).toList());
        response.setTotalCount(page.getTotalElements());
        return response;
    }

    @Cacheable(value = AppCacheProperties.CacheNames.ROOMS, key = "#id")
    public Room findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
    }

    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.ROOMS, allEntries = true)
    public RoomResponse add(UpsertRoomRequest request) {
        Room room = mapper.requestToRoom(request);
        return roomToResponse(repository.save(room));
    }

    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.ROOMS, allEntries = true)
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public RoomResponse roomToResponse(Room room) {
        return mapper.roomToResponse(room);
    }

    public RoomResponse update(Room room, UpsertRoomRequest request) {
        BeanUtils.copyNonNullProperties(
                mapper.requestToRoom(request),
                room
        );
        return roomToResponse(repository.save(room));
    }
}
