package hh.sof3.toWatch.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@MappedSuperclass
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String showId;

    @Column(name = "show_type")
    private String type;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Director is mandatory")
    private String director;

    @Column(name = "starring")
    private String cast;

    private String country;
    private String dateAdded;

    @Min(value = 1900, message = "Release year must be at least 1900")
    private int releaseYear;

    private String rating;
    private String duration;

    @NotBlank(message = "Genre is mandatory")
    private String listedIn;

    @NotBlank(message = "Description is mandatory")
    private String description;

    public Media(String showId, String type, String title, String director, String cast, String country,
            String dateAdded, int releaseYear, String rating, String duration, String listedIn, String description) {
        this.showId = showId;
        this.type = type;
        this.title = title;
        this.director = director;
        this.cast = cast;
        this.country = country;
        this.dateAdded = dateAdded;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.duration = duration;
        this.listedIn = listedIn;
        this.description = description;
    }

    public Media() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getListedIn() {
        return listedIn;
    }

    public void setListedIn(String listedIn) {
        this.listedIn = listedIn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    

}