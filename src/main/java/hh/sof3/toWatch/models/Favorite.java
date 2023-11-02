package hh.sof3.toWatch.models;

import jakarta.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "tvshow_id")
    private TVShow tvshow;

    // Constructors, getters, and setters

    public Favorite() {
    }

    public Favorite(User user, Movie movie, TVShow tvshow) {
        this.user = user;
        this.movie = movie;
        this.tvshow = tvshow;
    }

    // Getters and setters for id, user, movie, and tvshow

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public TVShow getTvshow() {
        return tvshow;
    }

    public void setTvshow(TVShow tvshow) {
        this.tvshow = tvshow;
    }
}