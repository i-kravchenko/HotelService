package com.example.HotelBooking.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@Document(collection = "new-statistic")
public class BookingStatistic
{
    private String id;
    private Long userId;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
}
