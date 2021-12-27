package com.skillbox.engine.service;

import com.skillbox.engine.api.response.PostResponse;
import com.skillbox.engine.model.DTO.PostDTO;
import com.skillbox.engine.model.DTO.SortModePost;
import com.skillbox.engine.model.DTO.UserDTO;
import com.skillbox.engine.model.entity.Post;
import com.skillbox.engine.model.entity.PostVotes;
import com.skillbox.engine.model.enums.PostViewMode;
import com.skillbox.engine.repository.PostRepository;
import org.jsoup.Jsoup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostsService {
    private final PostRepository postRepository;
    private final HashMap<PostViewMode, SortModePost> sortModePost = new HashMap<>();

    public PostsService(PostRepository postRepository) {
        this.postRepository = postRepository;
        initSort();
    }

    public PostResponse getAllPosts(int offset, int limit, String mode) {
        PostViewMode viewMode = PostViewMode.valueOf(mode.toUpperCase());
        int numberPage = offset / limit;
        PageRequest pageRequest = PageRequest.of(numberPage, limit);
        Page<Post> posts = sortModePost.get(viewMode).sort(pageRequest);
        List<PostDTO> postsDTO = posts.getContent().stream()
                .map(this::buildDTO)
                .collect(Collectors.toList());
        return PostResponse.builder()
                .count(posts.getTotalElements())
                .posts(postsDTO).build();
    }

    private void initSort() {
        sortModePost.put(PostViewMode.RECENT, (pageRequest) -> postRepository.findPostsOrderByDateDesc(pageRequest));
        sortModePost.put(PostViewMode.POPULAR, (pageRequest) -> postRepository.findPostsOrderByComment(pageRequest));
        sortModePost.put(PostViewMode.BEST, (pageRequest) -> postRepository.findPostsOrderByLikes(pageRequest));
        sortModePost.put(PostViewMode.EARLY, (pageRequest) -> postRepository.findPostsOrderByDateAsc(pageRequest));
    }


    private PostDTO buildDTO(Post post) {
        return PostDTO.builder()
                .time(post.getTime().toInstant().getEpochSecond())
                .user(UserDTO.builder()
                        .id(post.getUser().getId())
                        .name(post.getUser().getName())
                        .build())
                .title(post.getTitle())
                .announce(getAnounce(post.getText()))
                .likeCount(sumCountVotes(post.getPostVotes(), true))
                .dislikeCount(sumCountVotes(post.getPostVotes(), false))
                .commentCount(post.getPostComments().size())
                .viewCount(post.getViewCount())
                .build();
    }

    private String getAnounce(String text) {
        String textWithoutHtml = Jsoup.parse(text).text();
        return textWithoutHtml.length()>150 ? textWithoutHtml.substring(0, 150)+"...":textWithoutHtml+"...";
    }

    private int sumCountVotes(Collection<PostVotes> postVotes, boolean like) {
        if (like)
            return (int) postVotes.stream().filter(postVotes1 -> postVotes1.getValue() > 0).count();
        else
            return (int) postVotes.stream().filter(postVotes1 -> postVotes1.getValue() < 0).count();
    }

}
