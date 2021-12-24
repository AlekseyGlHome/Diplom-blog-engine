package com.skillbox.engine.model.DTO;

import com.skillbox.engine.model.entity.Post;
import org.springframework.data.domain.Page;

public interface SortModePost {
    Page<Post> sort();
}
