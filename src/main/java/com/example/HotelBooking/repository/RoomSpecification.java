package com.example.HotelBooking.repository;

import com.example.HotelBooking.dto.room.RoomRequest;
import com.example.HotelBooking.entity.Booking;
import com.example.HotelBooking.entity.Room;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public interface RoomSpecification
{
    static Specification<Room> withRequest(RoomRequest request) {
        return Specification
                .where(byRoomId(request.getRoomId()))
                .and(byName(request.getName()))
                .and(byPrice(request.getMinPrice(), request.getMaxPrice()))
                .and(byCapacity(request.getCapacity()))
                .and(byDates(request.getArrivalDate(), request.getDepartureDate()))
                .and(byHotelId(request.getHotelId()));
    }

    static Specification<Room> byRoomId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("id"), id);
        });
    }

    static Specification<Room> byName(String name) {
        return ((root, query, criteriaBuilder) -> {
            if (name == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("name"), name);
        });
    }

    static Specification<Room> byPrice(Integer minPrice, Integer maxPrice) {
        return ((root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            }
            if(maxPrice == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            if(minPrice == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
            return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
        });
    }

    static Specification<Room> byCapacity(Integer capacity) {
        return ((root, query, criteriaBuilder) -> {
            if (capacity == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("capacity"), capacity);
        });
    }

    static Specification<Room> byDates(LocalDate arrivalDate, LocalDate departureDate) {
        return ((root, query, criteriaBuilder) -> {
            if (arrivalDate == null || departureDate == null) {
                return null;
            }
            Join<Room, Booking> bookings = root.join("bookings", JoinType.LEFT);
            return criteriaBuilder.or(
                    criteriaBuilder.isNull(bookings),
                    criteriaBuilder.and(
                            criteriaBuilder.between(bookings.get("arrivalDate"), arrivalDate, departureDate).not(),
                            criteriaBuilder.between(bookings.get("departureDate"), arrivalDate, departureDate).not()
                    )
            );
        });
    }

    static Specification<Room> byHotelId(Long hotelId) {
        return ((root, query, criteriaBuilder) -> {
            if (hotelId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("hotel").get("id"), hotelId);
        });
    }
}
