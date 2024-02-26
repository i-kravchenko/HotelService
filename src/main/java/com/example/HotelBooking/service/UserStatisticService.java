package com.example.HotelBooking.service;

import com.example.HotelBooking.dto.event.UserRegisterEvent;
import com.example.HotelBooking.entity.UserStatistic;
import com.example.HotelBooking.repository.UserStatisticRepository;
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
public class UserStatisticService extends StatisticService<UserStatistic, String> {
    protected final UserStatisticRepository repository;

    public List<UserStatistic> findAll() {
        return repository.findAll();
    }

    @KafkaListener(
            topics = "${app.kafka.kafkaMessageUserRegisterTopic}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory"
    )
    public void listen(@Payload UserRegisterEvent event) {
        log.info("Received message: {}", event);
        UserStatistic userStatistic = UserStatistic.builder()
                .id(UUID.randomUUID().toString())
                .userId(event.getUserId())
                .build();
        repository.save(userStatistic);
    }
}
