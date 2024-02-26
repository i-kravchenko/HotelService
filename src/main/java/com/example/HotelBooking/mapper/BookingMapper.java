package com.example.HotelBooking.mapper;

import com.example.HotelBooking.dto.booking.BookingResponse;
import com.example.HotelBooking.dto.booking.UpsertBookingRequest;
import com.example.HotelBooking.dto.event.NewBookingEvent;
import com.example.HotelBooking.entity.Booking;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
                RoomMapper.class,
                UserMapper.class
        }
)
@DecoratedWith(BookingMapperDelegate.class)
public interface BookingMapper
{
    Booking requestToBooking(UpsertBookingRequest request);
    @Mapping(source = "id", target = "bookingId")
    BookingResponse bookingToResponse(Booking booking);

    @Mapping(source = "user.id", target = "userId")
    NewBookingEvent bookingToEvent(Booking booking);
}
