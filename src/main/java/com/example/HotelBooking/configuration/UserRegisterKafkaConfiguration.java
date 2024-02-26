package com.example.HotelBooking.configuration;

import com.example.HotelBooking.dto.event.UserRegisterEvent;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRegisterKafkaConfiguration extends KafkaConfiguration<UserRegisterEvent>
{

}
