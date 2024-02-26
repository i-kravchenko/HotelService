package com.example.HotelBooking.controller;


import com.example.HotelBooking.exception.NoStatisticDataException;
import com.example.HotelBooking.service.StatisticService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

public abstract class StatisticController
{
    public void downloadStatistic(HttpServletResponse response)
            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try {
            getService().download(response.getWriter());
            response.setContentType("text/csv");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + getFileName() + "\"");
        } catch (NoStatisticDataException e) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    protected abstract StatisticService<?, ?> getService();
    protected abstract String getFileName();
}
