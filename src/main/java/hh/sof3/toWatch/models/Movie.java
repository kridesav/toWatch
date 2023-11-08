package hh.sof3.toWatch.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Movie extends Media {

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favourite> favourites = new ArrayList<>();

    public Movie(String showId, String type, String title, String director, String cast, String country,
            String dateAdded, int releaseYear, String rating, String duration, String listedIn, String description) {
        super(showId, type, title, director, cast, country, dateAdded, releaseYear, rating, duration, listedIn,
                description);
    }

    public Movie() {
    }

}