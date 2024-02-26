package com.example.HotelBooking.repository;

import com.example.HotelBooking.dto.hotel.HotelRequest;
import com.example.HotelBooking.entity.Hotel;
import org.springframework.data.jpa.domain.Specification;

public interface HotelSpecification
{
    static Specification<Hotel> withRequest(HotelRequest request) {
        return Specification
                .where(byHotelId(request.getHotelId()))
                .and(byName(request.getName()))
                .and(byTitle(request.getAdsTitle()))
                .and(byCity(request.getCity()))
                .and(byAddress(request.getAddress()))
                .and(byDistance(request.getDistanceFromCentre()))
                .and(byRate(request.getRating(), request.getNumberOfRating()));
    }

    static Specification<Hotel> byHotelId(Long hotelId) {
        return (root, query, criteriaBuilder) -> {
            if (hotelId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("id"), hotelId);
        };
    }

    static Specification<Hotel> byName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("name"), name);
        };
    }

    static Specification<Hotel> byTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("adsTitle"), title);
        };
    }

    static Specification<Hotel> byCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("city"), city);
        };
    }

    static Specification<Hotel> byAddress(String address) {
        return (root, query, criteriaBuilder) -> {
            if (address == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("address"), address);
        };
    }

    static Specification<Hotel> byDistance(Integer distance) {
        return (root, query, criteriaBuilder) -> {
            if (distance == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("distance"), distance);
        };
    }

    static Specification<Hotel> byRate(Double rating, Integer numberOfRating) {
        return (root, query, criteriaBuilder) -> {
            if(rating == null && numberOfRating == null)  {
                return null;
            }
            if(numberOfRating == null)  {
                return criteriaBuilder.equal(root.get("rating"), rating);
            }
            if(rating == null) {
                return criteriaBuilder.equal(root.get("numberOfRating"), numberOfRating);
            }
            return  criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("rating"), rating),
                    criteriaBuilder.equal(root.get("numberOfRating"), numberOfRating)
            );
        };
    }
}
