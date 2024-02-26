package com.example.HotelBooking.mapper;

import com.example.HotelBooking.dto.room.RoomResponse;
import com.example.HotelBooking.dto.room.UpsertRoomRequest;
import com.example.HotelBooking.entity.Room;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(RoomMapperDelegate.class)
public interface RoomMapper
{
    Room requestToRoom(UpsertRoomRequest request);

    @Mapping(source = "id", target = "roomId")
    @Mapping(source = "hotel.id", target = "hotelId")
    RoomResponse roomToResponse(Room room);
}
