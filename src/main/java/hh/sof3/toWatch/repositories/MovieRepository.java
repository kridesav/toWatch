package hh.sof3.toWatch.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hh.sof3.toWatch.models.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query(value = "SELECT * FROM Movie ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<Movie> findRandomMovies();

    List<Movie> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT m FROM Movie m ORDER BY SIZE(m.favourites) DESC")
    List<Movie> findMostFavoritedMovies(Pageable pageable);
}