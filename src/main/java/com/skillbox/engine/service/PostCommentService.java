package com.skillbox.engine.service;

import com.skillbox.engine.model.DTO.CommentDTO;
import com.skillbox.engine.model.entity.PostComment;
import com.skillbox.engine.repository.PostCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final UserService userService;
    private final PostCommentRepository postCommentRepository;

    public List<PostComment> getPostCommentsByPostId(int postId) {
        return postCommentRepository.getPostCommentsByPostId(postId);
    }

    public CommentDTO buildCommentTDO(PostComment postComment) {
        return CommentDTO.builder()
                .id(postComment.getId())
                .time(postComment.getTime().toInstant().getEpochSecond())
                .text(postComment.getText())
                .user(userService.buildUserPhotoDTO(postComment.getUser()))
                .build();
    }
}
