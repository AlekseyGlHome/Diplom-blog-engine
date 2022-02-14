package com.skillbox.engine.repository;

import com.skillbox.engine.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select count(u) from User u where u.email = ?1")
    long countByEmailEquals(String email);


    @Query("select u from User u where u.email = ?1")
    Optional<User> findByEmail(String email);

}
