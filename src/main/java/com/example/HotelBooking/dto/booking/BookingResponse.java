package com.example.HotelBooking.dto.booking;

import com.example.HotelBooking.dto.room.RoomResponse;
import com.example.HotelBooking.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse
{
    private Long bookingId;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private RoomResponse room;
    private UserResponse user;
}
