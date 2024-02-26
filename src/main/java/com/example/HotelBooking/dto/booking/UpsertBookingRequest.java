package com.example.HotelBooking.dto.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertBookingRequest
{
    @NotNull(message = "arrivalDate is required")
    private LocalDate arrivalDate;
    @NotNull(message = "departureDate is required")
    private LocalDate departureDate;
    @NotNull(message = "roomId is required")
    private Long roomId;
    @NotNull(message = "userId is required")
    private Long userId;
}
