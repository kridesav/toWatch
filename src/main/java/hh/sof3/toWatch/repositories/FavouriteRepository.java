package hh.sof3.toWatch.repositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import hh.sof3.toWatch.models.Favourite;
import hh.sof3.toWatch.models.Movie;
import hh.sof3.toWatch.models.TVShow;
import hh.sof3.toWatch.models.User;

@RepositoryRestResource
public interface FavouriteRepository extends CrudRepository<Favourite, Long>{

    Favourite findByUserAndMovie(User user, Movie movie);
    Favourite findByUserAndTvshow(User user, TVShow tvshow);
}