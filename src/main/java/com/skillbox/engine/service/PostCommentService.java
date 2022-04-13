package com.skillbox.engine.service;

import com.skillbox.engine.api.request.CommentRequest;
import com.skillbox.engine.api.response.CommentResponse;
import com.skillbox.engine.exception.CommentException;
import com.skillbox.engine.exception.NotFoundException;
import com.skillbox.engine.model.DTO.CommentDTO;
import com.skillbox.engine.model.entity.Post;
import com.skillbox.engine.model.entity.PostComment;
import com.skillbox.engine.model.entity.User;
import com.skillbox.engine.repository.PostCommentRepository;
import com.skillbox.engine.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final UserService userService;
    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;

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

    public CommentResponse addPostComment(String userEmail, CommentRequest comment) throws CommentException, NotFoundException {
        Post post = postRepository.findById(comment.getPostId())
                .orElseThrow(()->new CommentException("Пост не существует"));
        PostComment parentComment = null;
        User currentUser = userService.findByEmail(userEmail)
                .orElseThrow(()->new NotFoundException("User not found"));

        if (comment.getParentId()!=null){
            parentComment = postCommentRepository.findById(comment.getParentId())
                   .orElseThrow(()->new CommentException("Комментарий не существует"));
        }
        if (comment.getText().isBlank() || comment.getText().length()<10){
            throw new CommentException("Текст комментария не задан или слишком короткий");
        }
        PostComment postComment = new PostComment();
        postComment.setPost(post);
        postComment.setParent(parentComment);
        postComment.setText(comment.getText());
        postComment.setTime(Timestamp.valueOf(LocalDateTime.now()));
        postComment.setUser(currentUser);
        return new CommentResponse(postCommentRepository.save(postComment).getId());
    }
}
