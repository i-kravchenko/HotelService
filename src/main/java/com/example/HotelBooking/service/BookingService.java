package com.example.HotelBooking.service;

import com.example.HotelBooking.dto.Request;
import com.example.HotelBooking.dto.event.NewBookingEvent;
import com.example.HotelBooking.dto.room.RoomRequest;
import com.example.HotelBooking.entity.Booking;
import com.example.HotelBooking.mapper.BookingMapper;
import com.example.HotelBooking.repository.BookingRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService
{
    @Value("${app.kafka.kafkaMessageNewBookingTopic}")
    private String topic;
    private final BookingRepository repository;
    private final RoomService roomService;
    private final BookingMapper mapper;
    private final KafkaTemplate<String, NewBookingEvent> kafkaTemplate;

    public Page<Booking> findAll(Request request) {
        return repository.findAll(
                PageRequest.of(request.getPageNumber(), request.getPageSize())
        );
    }

    public Booking bookRoom(Booking booking) {
        RoomRequest request = RoomRequest.builder()
                .roomId(booking.getRoom().getId())
                .arrivalDate(booking.getArrivalDate())
                .departureDate(booking.getDepartureDate())
                .build();
        request.setPageNumber(0);
        request.setPageSize(1);
        if(roomService.findAll(request).getContent().isEmpty()) {
            throw new EntityExistsException("The room is occupied. Choose other dates or room!");
        }
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), mapper.bookingToEvent(booking));
        return repository.save(booking);
    }
}
