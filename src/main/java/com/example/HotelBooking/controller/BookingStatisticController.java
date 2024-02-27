package com.example.HotelBooking.controller;

import com.example.HotelBooking.service.BookingStatisticService;
import com.example.HotelBooking.service.StatisticService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/api/v1/statistic/booking")
@RequiredArgsConstructor
public class BookingStatisticController extends StatisticController
{
    protected final BookingStatisticService service;

    @Override
    @GetMapping
    public void downloadStatistic(HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        log.info("method GET /api/v1/statistic/booking was called");
        super.downloadStatistic(response);
    }

    @Override
    protected StatisticService<?, ?> getService() {
        return service;
    }

    @Override
    protected String getFileName() {
        return "booking.csv";
    }
}
