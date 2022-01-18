package com.skillbox.engine.repository;

import com.skillbox.engine.model.entity.PostVotes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostVotesRepository extends JpaRepository<PostVotes, Integer> {
}
