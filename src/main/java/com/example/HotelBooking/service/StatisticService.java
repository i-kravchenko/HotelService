package com.example.HotelBooking.service;

import com.example.HotelBooking.exception.NoStatisticDataException;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.PrintWriter;
import java.util.List;

public abstract class StatisticService<T, I>
{
    public void download(PrintWriter printWriter) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        List<T> data = findAll();
        if(data.isEmpty()) {
            throw new NoStatisticDataException("Statistic is empty");
        }
        StatefulBeanToCsv<T> writer = new StatefulBeanToCsvBuilder<T>(printWriter)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();
        writer.write(data);
    }

    protected abstract List<T> findAll();
}
