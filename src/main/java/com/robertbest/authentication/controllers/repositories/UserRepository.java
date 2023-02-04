package com.robertbest.authentication.controllers.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.robertbest.authentication.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	
	Optional<User> findByEmail(String email);
}
