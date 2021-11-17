package com.skillbox.engine.repository;

import com.skillbox.engine.model.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}
