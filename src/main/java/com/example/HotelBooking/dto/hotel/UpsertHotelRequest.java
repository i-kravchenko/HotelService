package com.example.HotelBooking.dto.hotel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertHotelRequest
{
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "adsTitle is required")
    private String adsTitle;
    @NotBlank(message = "city is required")
    private String city;
    @NotBlank(message = "address is required")
    private String address;
    @NotNull(message = "distanceFromCentre is required")
    private Integer distanceFromCentre;
}
