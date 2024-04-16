package com.example.moviecatalogservice.resources;

import com.example.moviecatalogservice.models.CatalogItem;
import com.example.moviecatalogservice.models.Movie;
import com.example.moviecatalogservice.models.Rating;
import com.example.moviecatalogservice.models.UserRating;
import com.example.moviecatalogservice.services.MovieInfo;
import com.example.moviecatalogservice.services.UserRatingInfo;
import com.netflix.discovery.converters.Auto;
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

    @Autowired
    MovieInfo movieInfo;

    @Autowired
    UserRatingInfo userRatingInfo;

    // For Reactive demo
    // @Autowired
    // private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    //@HystrixCommand(fallbackMethod = "getCatalogFallback")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        // Make call to movie info service for each
        // movieid in ratings to get movie name
        // and combine with rating from ratings list
        UserRating userRating = userRatingInfo.getUserRating(userId);
        return userRating.getRatings().stream()
                .map(rating -> movieInfo.getCatalogItem(rating))
                .collect(Collectors.toList());
    }

//    public List<CatalogItem> getCatalogFallback(@PathVariable("userId") String userId, Throwable throwable) {
//        return Arrays.asList(new CatalogItem("No movie","",0));
//    }

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
