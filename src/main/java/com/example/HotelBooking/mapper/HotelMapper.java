package com.example.HotelBooking.mapper;

import com.example.HotelBooking.dto.hotel.HotelResponse;
import com.example.HotelBooking.dto.hotel.UpsertHotelRequest;
import com.example.HotelBooking.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper
{
    Hotel requestToHotel(UpsertHotelRequest request);
    @Mapping(source = "id", target = "hotelId")
    HotelResponse hotelToResponse(Hotel hotel);
}
