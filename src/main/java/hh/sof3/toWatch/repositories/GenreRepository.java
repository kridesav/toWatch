package hh.sof3.toWatch.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import hh.sof3.toWatch.models.Genre;

@RepositoryRestResource
public interface GenreRepository extends CrudRepository<Genre, Long>{
    Genre findByName(String name);
}