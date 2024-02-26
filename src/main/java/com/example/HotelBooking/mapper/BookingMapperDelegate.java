package com.example.HotelBooking.mapper;

import com.example.HotelBooking.dto.booking.BookingResponse;
import com.example.HotelBooking.dto.booking.UpsertBookingRequest;
import com.example.HotelBooking.dto.event.NewBookingEvent;
import com.example.HotelBooking.entity.Booking;
import com.example.HotelBooking.entity.Room;
import com.example.HotelBooking.entity.User;
import com.example.HotelBooking.service.RoomService;
import com.example.HotelBooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class BookingMapperDelegate implements BookingMapper
{
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;
    @Autowired
    @Qualifier("delegate")
    private BookingMapper delegate;

    @Override
    public Booking requestToBooking(UpsertBookingRequest request) {
        Booking booking = delegate.requestToBooking(request);
        User user = userService.findById(request.getUserId());
        Room room = roomService.findById(request.getRoomId());
        booking.setUser(user);
        booking.setRoom(room);
        return booking;
    }

    @Override
    public BookingResponse bookingToResponse(Booking booking) {
        return delegate.bookingToResponse(booking);
    }

    @Override
    public NewBookingEvent bookingToEvent(Booking booking) {
        return delegate.bookingToEvent(booking);
    }
}
