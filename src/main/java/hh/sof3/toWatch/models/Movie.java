package hh.sof3.toWatch.models;

import jakarta.persistence.Entity;

@Entity
public class Movie extends Media {

    public Movie(String showId, String type, String title, String director, String cast, String country,
            String dateAdded, int releaseYear, String rating, String duration, String listedIn, String description) {
        super(showId, type, title, director, cast, country, dateAdded, releaseYear, rating, duration, listedIn, description);
    }

    public Movie() {
    }

}