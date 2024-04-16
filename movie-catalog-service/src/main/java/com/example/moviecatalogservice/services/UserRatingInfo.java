package com.example.moviecatalogservice.services;

import com.example.moviecatalogservice.models.Rating;
import com.example.moviecatalogservice.models.UserRating;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class UserRatingInfo {

    // Used for marshalling REST API calls into objects
    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "get-user-rating-fallback", fallbackMethod = "getUserRatingFallback")
    public UserRating getUserRating(@PathVariable("userId") String userId) {
        // Get all related movie IDs for given userId
        // and create Rating objects using that data
        // This would usually be done with call to database
        // but is hardcoded as project is focused on microservices
        return restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/foo" + userId, UserRating.class);
    }

    public UserRating getUserRatingFallback(@PathVariable("userId") String userId, Throwable e) {
        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setRatings(Arrays.asList(
                new Rating("0", 0)
        ));
        return userRating;
    }
}
