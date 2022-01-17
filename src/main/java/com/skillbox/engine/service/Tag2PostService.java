package com.skillbox.engine.service;

import com.skillbox.engine.model.DTO.TagResponseRepository;
import com.skillbox.engine.model.entity.Tag;
import com.skillbox.engine.repository.Tag2PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Tag2PostService {

    private final Tag2PostRepository tag2PostRepository;

    public List<TagResponseRepository> countOfPostsByTags() {
        return tag2PostRepository.countOfPostsByTags();
    }

    public long countActivePost() {
        return tag2PostRepository.countOfActivePostOnTags();
    }

    public List<Tag> getPostTags(int postId){
        return tag2PostRepository.getTagsByPostId(postId);
    }

}
