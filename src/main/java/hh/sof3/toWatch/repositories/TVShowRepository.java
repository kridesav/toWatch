package hh.sof3.toWatch.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hh.sof3.toWatch.models.TVShow;

@Repository
public interface TVShowRepository extends JpaRepository<TVShow, Long> {
    @Query(value = "SELECT * FROM TvShow ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<TVShow> findRandomTvShows();

    List<TVShow> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT t FROM TVShow t ORDER BY SIZE(t.favourites) DESC")
    List<TVShow> findMostFavoritedTVShows(Pageable pageable);
}