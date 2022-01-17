package com.skillbox.engine.repository;

import com.skillbox.engine.model.DTO.TagResponseRepository;
import com.skillbox.engine.model.entity.Tag;
import com.skillbox.engine.model.entity.Tag2Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Tag2PostRepository extends JpaRepository<Tag2Post, Integer> {
    @Query("SELECT tp.tag.name AS name, COUNT (tp.tag) AS count " +
            "FROM Tag2Post tp " +
            "WHERE tp.post.isActive = 1 and " +
            "tp.post.moderationStatus=com.skillbox.engine.model.enums.PostModerationStatus.ACCEPTED " +
            "GROUP BY tp.tag")
    List<TagResponseRepository> countOfPostsByTags();

    @Query("SELECT COUNT(tp.post) as count " +
            "FROM Tag2Post tp " +
            "WHERE tp.post.isActive = 1 and " +
            "tp.post.moderationStatus=com.skillbox.engine.model.enums.PostModerationStatus.ACCEPTED ")
    long countOfActivePostOnTags();

    @Query("select t.tag from Tag2Post t where t.post.id = :postId")
    List<Tag> getTagsByPostId(Integer postId);
}
