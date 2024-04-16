package com.example.moviecatalogservice.services;

import com.example.moviecatalogservice.models.CatalogItem;
import com.example.moviecatalogservice.models.Movie;
import com.example.moviecatalogservice.models.Rating;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfo {

    // Used for marshalling REST API calls into objects
    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "get-catalog-item-fallback", fallbackMethod = "getCatalogItemFallback")
    public CatalogItem getCatalogItem(Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName(), "Desc", rating.getRating());
    }

    public CatalogItem getCatalogItemFallback(Rating rating, Throwable e) {
        return new CatalogItem("Movie name not found", "", rating.getRating());
    }
}
