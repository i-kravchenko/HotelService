package com.example.HotelBooking.mapper;

import com.example.HotelBooking.dto.booking.BookingResponse;
import com.example.HotelBooking.dto.booking.UpsertBookingRequest;
import com.example.HotelBooking.dto.event.NewBookingEvent;
import com.example.HotelBooking.entity.Booking;
import com.example.HotelBooking.repository.RoomRepository;
import com.example.HotelBooking.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
                RoomMapper.class,
                UserMapper.class
        }
)
public abstract class BookingMapper
{
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected RoomRepository roomRepository;


    @Mappings({
            @Mapping(target = "user",
                    expression = "java(userRepository.findById(request.getUserId()).get())"),
            @Mapping(target = "room",
                    expression = "java(roomRepository.findById(request.getRoomId()).get())"),
    })
    public abstract Booking requestToBooking(UpsertBookingRequest request);
    @Mapping(source = "id", target = "bookingId")
    public abstract BookingResponse bookingToResponse(Booking booking);

    @Mapping(source = "user.id", target = "userId")
    public abstract NewBookingEvent bookingToEvent(Booking booking);
}
