package com.example.HotelBooking.configuration;

import com.example.HotelBooking.dto.event.NewBookingEvent;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NewBookingKafkaConfiguration extends KafkaConfiguration<NewBookingEvent>
{
}
