package com.example.HotelBooking.dto.hotel;

import com.example.HotelBooking.dto.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelRequest extends Request
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
