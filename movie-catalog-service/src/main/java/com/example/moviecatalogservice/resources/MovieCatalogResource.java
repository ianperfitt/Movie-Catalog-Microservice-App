package com.example.moviecatalogservice.resources;

import com.example.moviecatalogservice.models.CatalogItem;
import com.example.moviecatalogservice.models.Movie;
import com.example.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


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
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        // Get all related movie IDs for given userId
        // and create Rating objects using that data
        // This would usually be done with call to database
        // but is hardcoded as project is focused on microservices
        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/foo" + userId, UserRating.class);

        // Make call to movie info service for each
        // movieid in ratings to get movie name
        // and combine with rating from ratings list
        return ratings.getUserRating().stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);

            return new CatalogItem(movie.getName(), "Desc", rating.getRating());
        })
        .collect(Collectors.toList());
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
