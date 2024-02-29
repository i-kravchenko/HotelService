package com.example.HotelBooking.service;

import com.example.HotelBooking.dto.Request;
import com.example.HotelBooking.dto.ResponseList;
import com.example.HotelBooking.dto.booking.BookingResponse;
import com.example.HotelBooking.dto.booking.UpsertBookingRequest;
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

    public ResponseList<BookingResponse> findAll(Request request) {
        Page<Booking> page = repository.findAll(
                PageRequest.of(request.getPageNumber(), request.getPageSize())
        );
        ResponseList<BookingResponse> response = new ResponseList<>();
        response.setItems(page.getContent().stream().map(mapper::bookingToResponse).toList());
        response.setTotalCount(page.getTotalElements());
        return response;
    }

    public BookingResponse bookRoom(UpsertBookingRequest request) {
        Booking booking = mapper.requestToBooking(request);
        RoomRequest roomRequest = RoomRequest.builder()
                .roomId(booking.getRoom().getId())
                .arrivalDate(booking.getArrivalDate())
                .departureDate(booking.getDepartureDate())
                .build();
        roomRequest.setPageNumber(0);
        roomRequest.setPageSize(1);
        if(roomService.findAll(roomRequest).getItems().isEmpty()) {
            throw new EntityExistsException("The room is occupied. Choose other dates or room!");
        }
        booking = repository.save(booking);
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), mapper.bookingToEvent(booking));
        return mapper.bookingToResponse(booking);
    }
}
