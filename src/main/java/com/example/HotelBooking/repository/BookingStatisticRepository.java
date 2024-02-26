package com.example.HotelBooking.repository;

import com.example.HotelBooking.entity.BookingStatistic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookingStatisticRepository extends MongoRepository<BookingStatistic, String>
{
}
