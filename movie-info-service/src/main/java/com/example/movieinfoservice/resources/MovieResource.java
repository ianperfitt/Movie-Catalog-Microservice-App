package com.example.movieinfoservice.resources;

import com.example.movieinfoservice.models.Movie;
import com.example.movieinfoservice.models.MovieSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId){
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("accept", "application/json");
//        headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjOTRlZGU5N2I4YTZmYWYxYzFmNjEwMmIwYmNjMjFmZCIsInN1YiI6IjY2MWQ1M2JlZTQ4ODYwMDE4NTNiOTY3MCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.-9Tt9VIkBKARm1UfwP_niMA3SNAYf70_dYb1xvekNHw");
//
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;
//        // String url = "https://api.themoviedb.org/3/authentication";
//        ResponseEntity<MovieSummary> response = restTemplate.exchange(url, HttpMethod.GET,
//                requestEntity, MovieSummary.class);
//        MovieSummary movieSummary = response.getBody();
        MovieSummary movieSummary = restTemplate.getForObject("https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey
                ,MovieSummary.class
        );
        return new Movie(movieId, movieSummary.getTitle(),movieSummary.getOverview());
    }
}
