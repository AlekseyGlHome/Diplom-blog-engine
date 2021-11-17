package com.skillbox.engine.repository;

import com.skillbox.engine.model.entity.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Integer> {

}
