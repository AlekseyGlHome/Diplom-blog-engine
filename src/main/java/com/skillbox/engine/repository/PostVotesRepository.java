package com.skillbox.engine.repository;

import com.skillbox.engine.model.entity.PostVotes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostVotesRepository extends JpaRepository<PostVotes, Integer> {

    Optional<PostVotes> findByPostIdAndUserId(int postId, int userId);
}
