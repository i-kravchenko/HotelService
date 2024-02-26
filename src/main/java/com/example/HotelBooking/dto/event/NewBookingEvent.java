package com.example.HotelBooking.dto.event;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewBookingEvent
{
    private Long userId;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
}
