package com.skillbox.engine.repository;

import com.skillbox.engine.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select count(u) from User u where u.eMail = ?1")
    long countByEmailEquals(String email);

}
