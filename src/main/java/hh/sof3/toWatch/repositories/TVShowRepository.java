package hh.sof3.toWatch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hh.sof3.toWatch.models.TVShow;

@Repository
public interface TVShowRepository extends JpaRepository<TVShow, Long>{
}