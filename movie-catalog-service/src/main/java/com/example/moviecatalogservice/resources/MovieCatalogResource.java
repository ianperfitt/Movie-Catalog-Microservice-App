package com.example.moviecatalogservice.resources;

import com.example.moviecatalogservice.models.CatalogItem;
import com.example.moviecatalogservice.models.Movie;
import com.example.moviecatalogservice.models.Rating;
import com.example.moviecatalogservice.models.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    // Used for marshalling REST API calls into objects
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    // For Reactive demo
    // @Autowired
    // private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    @CircuitBreaker(name = "get-catalog-fallback", fallbackMethod = "getCatalogFallback")
    //@HystrixCommand(fallbackMethod = "getCatalogFallback")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        // Make call to movie info service for each
        // movieid in ratings to get movie name
        // and combine with rating from ratings list
        UserRating userRating = getUserRating(userId);
        return userRating.getRatings().stream()
                .map(rating -> {
                    return getCatalog(rating);
                }).collect(Collectors.toList())
    }

    private CatalogItem getCatalogItem(Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName(), "Desc", rating.getRating());
    }

    private UserRating getUserRating(@PathVariable("userId") String userId) {
        // Get all related movie IDs for given userId
        // and create Rating objects using that data
        // This would usually be done with call to database
        // but is hardcoded as project is focused on microservices
        return restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/foo" + userId, UserRating.class);

    }



    public List<CatalogItem> getCatalogFallback(@PathVariable("userId") String userId, Throwable throwable) {
        return Arrays.asList(new CatalogItem("No movie","",0));
    }


    // Reactive demo code
        // Movie movie = webClientBuilder.build()
        //  .get()
        //  .uri("http://localhost:8082/movies/" + rating.getMovieId())
        //  .retrieve()
        //  .bodyToMono(Movie.class)
        // This is used to make code synchronous for demo purposes
        // even though we are using Reactive asynchronous processes
        //   .block();
}
