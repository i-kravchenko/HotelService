package com.example.HotelBooking.repository;

import com.example.HotelBooking.entity.UserStatistic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatisticRepository extends MongoRepository<UserStatistic, String>
{
}
