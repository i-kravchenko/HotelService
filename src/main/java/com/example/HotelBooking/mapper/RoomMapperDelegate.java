package com.example.HotelBooking.mapper;

import com.example.HotelBooking.dto.room.RoomResponse;
import com.example.HotelBooking.dto.room.UpsertRoomRequest;
import com.example.HotelBooking.entity.Hotel;
import com.example.HotelBooking.entity.Room;
import com.example.HotelBooking.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class RoomMapperDelegate implements RoomMapper
{
    @Autowired
    private HotelService hotelService;
    @Autowired
    @Qualifier("delegate")
    private RoomMapper delegate;

    @Override
    public Room requestToRoom(UpsertRoomRequest request)
    {
        Room room = delegate.requestToRoom(request);
        if(request.getHotelId() != null) {
            Hotel hotel = hotelService.findById(request.getHotelId());
            room.setHotel(hotel);
        }
        return room;
    }

    @Override
    public RoomResponse roomToResponse(Room room) {
        return delegate.roomToResponse(room);
    }
}
