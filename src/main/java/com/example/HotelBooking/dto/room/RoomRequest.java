package com.example.HotelBooking.dto.room;

import com.example.HotelBooking.dto.Request;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest extends Request
{
    private Long roomId;
    private String name;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer capacity;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private Long hotelId;
}
