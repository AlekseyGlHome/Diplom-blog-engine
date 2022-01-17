package com.skillbox.engine.repository;

import com.skillbox.engine.model.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {

    List<PostComment> getPostCommentsByPostId(Integer postId);
}
