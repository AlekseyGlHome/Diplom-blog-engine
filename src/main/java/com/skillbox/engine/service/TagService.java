package com.skillbox.engine.service;

import com.skillbox.engine.api.response.TagResponse;
import com.skillbox.engine.model.DTO.TagDTO;
import com.skillbox.engine.model.DTO.TagResponseRepository;

import com.skillbox.engine.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final Tag2PostService tag2PostService;

    public TagResponse getAllTags() {
        TagResponse tagResponse = new TagResponse();
        long countPost = tag2PostService.countActivePost();
        List<TagResponseRepository> tags = tag2PostService.countOfPostsByTags();
        Optional<TagResponseRepository> tagInterface = tags.stream().
                max(Comparator.comparingLong(TagResponseRepository::getCount));
        double countMax = tagInterface.map(TagResponseRepository::getCount).orElse(1);
        double coefficient = 1 / (countMax / countPost);
        List<TagDTO> tagDTOS = tags.stream().map(tag -> {
            double weight = (double) tag.getCount() / countPost;
            weight = weight * coefficient;
            return new TagDTO(tag.getName(), weight);
        }).collect(Collectors.toList());

        tagResponse.setTags(tagDTOS);
        return tagResponse;
    }
}
