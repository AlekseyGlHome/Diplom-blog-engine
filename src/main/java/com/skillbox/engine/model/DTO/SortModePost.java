package com.skillbox.engine.model.DTO;

import com.skillbox.engine.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface SortModePost {
    Page<Post> sort(PageRequest pageRequest, Timestamp timestamp);
}
