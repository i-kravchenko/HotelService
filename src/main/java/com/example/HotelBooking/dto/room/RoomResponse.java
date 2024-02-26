package com.example.HotelBooking.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse
{
    private Long roomId;
    private String name;
    private String description;
    private Integer number;
    private Integer price;
    private Integer capacity;
    private LocalDate unavailableAt;
    private Long hotelId;
}
