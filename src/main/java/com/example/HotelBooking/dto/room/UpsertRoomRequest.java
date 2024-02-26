package com.example.HotelBooking.dto.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertRoomRequest
{
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "description is required")
    private String description;
    @NotNull(message = "number is required")
    private Integer number;
    @NotNull(message = "price is required")
    private Integer price;
    @NotNull(message = "capacity is required")
    private Integer capacity;
    @NotNull(message = "unavailableAt is required")
    private LocalDate unavailableAt;
    @NotNull(message = "hotelId is required")
    private Long hotelId;
}
