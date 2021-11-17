package com.skillbox.engine.repository;

import com.skillbox.engine.model.entity.PostComment;
import org.springframework.data.repository.CrudRepository;

public interface PostCommentRepository extends CrudRepository<PostComment, Integer> {

}
