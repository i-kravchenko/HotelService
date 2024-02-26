package com.example.HotelBooking.controller;

import com.example.HotelBooking.service.StatisticService;
import com.example.HotelBooking.service.UserStatisticService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/statistic/users")
@RequiredArgsConstructor
public class UserStatisticController extends StatisticController
{
    private final UserStatisticService service;

    @Override
    @GetMapping
    public void downloadStatistic(HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        super.downloadStatistic(response);
    }

    @Override
    protected StatisticService<?, ?> getService() {
        return service;
    }

    @Override
    protected String getFileName() {
        return "users.csv";
    }
}
