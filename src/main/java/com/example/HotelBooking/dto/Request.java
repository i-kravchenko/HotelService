package com.example.HotelBooking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request
{
    @NotNull(message = "pageSize is required")
    protected Integer pageSize;
    @NotNull(message = "pageNumber is required")
    protected Integer pageNumber;
}
