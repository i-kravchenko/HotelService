package com.example.HotelBooking.mapper;

import com.example.HotelBooking.dto.event.UserRegisterEvent;
import com.example.HotelBooking.dto.user.UpsertUserRequest;
import com.example.HotelBooking.dto.user.UserResponse;
import com.example.HotelBooking.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper
{
    User requestToUser(UpsertUserRequest request);
    @Mapping(source = "id", target = "userId")
    UserResponse userToResponse(User user);

    @Mapping(source = "id", target = "userId")
    UserRegisterEvent userToEvent(User user);
}
