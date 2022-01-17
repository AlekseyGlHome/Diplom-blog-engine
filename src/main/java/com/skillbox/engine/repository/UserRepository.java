package com.skillbox.engine.repository;

import com.skillbox.engine.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
