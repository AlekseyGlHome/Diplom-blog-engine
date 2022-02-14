package com.skillbox.engine.model.DTO;

import com.skillbox.engine.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface SortModeMyPost {
    Page<Post> sort(PageRequest pageRequest, int userId);
}
