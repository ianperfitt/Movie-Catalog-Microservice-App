package com.example.moviecatalogservice.models;

import java.util.List;

public class UserRating {

    private List<Rating> userRating;

    public List<Rating> getRatings() {
        return userRating;
    }

    public void setRatings(List<Rating> userRating) {
        this.userRating = userRating;
    }
}
