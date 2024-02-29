package com.example.HotelBooking.dto.hotel;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponse implements Serializable
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
