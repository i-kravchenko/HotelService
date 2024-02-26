package com.example.HotelBooking.dto.hotel;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponse
{
    private Long hotelId;
    private String name;
    private String adsTitle;
    private String city;
    private String address;
    private Integer distanceFromCentre;
    private Double rating;
    private Integer numberOfRating;
}
