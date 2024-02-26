package com.example.HotelBooking.service;

import com.example.HotelBooking.dto.event.NewBookingEvent;
import com.example.HotelBooking.entity.BookingStatistic;
import com.example.HotelBooking.repository.BookingStatisticRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingStatisticService extends StatisticService<BookingStatistic, String>
{
    private final BookingStatisticRepository repository;

    @Override
    protected List<BookingStatistic> findAll() {
        return repository.findAll();
    }

    @KafkaListener(
            topics = "${app.kafka.kafkaMessageNewBookingTopic}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory"
    )
    public void listen(@Payload NewBookingEvent event) {
        log.info("Received message: {}", event);
        BookingStatistic bookingStatistic = BookingStatistic.builder()
                .id(UUID.randomUUID().toString())
                .userId(event.getUserId())
                .arrivalDate(event.getArrivalDate())
                .departureDate(event.getDepartureDate())
                .build();
        repository.save(bookingStatistic);
    }
}
