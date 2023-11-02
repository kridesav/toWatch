package hh.sof3.toWatch.repositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import hh.sof3.toWatch.models.Favorite;

@RepositoryRestResource
public interface FavoriteRepository extends CrudRepository<Favorite, Long>{
}