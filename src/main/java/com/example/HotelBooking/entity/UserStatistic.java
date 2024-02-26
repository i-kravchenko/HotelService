package com.example.HotelBooking.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "users-statistic")
public class UserStatistic
{
    private String id;
    private Long userId;
}
