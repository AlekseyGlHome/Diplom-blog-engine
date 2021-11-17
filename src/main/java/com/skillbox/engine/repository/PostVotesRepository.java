package com.skillbox.engine.repository;

import com.skillbox.engine.model.entity.PostVotes;
import org.springframework.data.repository.CrudRepository;

public interface PostVotesRepository extends CrudRepository<PostVotes, Integer> {
}
