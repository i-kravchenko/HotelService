package com.example.HotelBooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hotel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String adsTitle;
    private String city;
    private String address;
    private Integer distanceFromCentre;
    private Double rating;
    private Integer numberOfRating;
}
