package hh.sof3.toWatch.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import hh.sof3.toWatch.models.User;

@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long>{
    User findByUsername(String username);
}