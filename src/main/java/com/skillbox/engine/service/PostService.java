package com.skillbox.engine.service;

import com.skillbox.engine.api.request.PostRequest;
import com.skillbox.engine.api.response.CalendarResponse;
import com.skillbox.engine.api.response.PostDetailResponse;
import com.skillbox.engine.api.response.PostResponse;
import com.skillbox.engine.api.response.PostUpdateResponse;
import com.skillbox.engine.exception.NotFoundException;
import com.skillbox.engine.model.DTO.*;
import com.skillbox.engine.model.entity.*;
import com.skillbox.engine.model.enums.ModerationStatus;
import com.skillbox.engine.model.enums.PostModerationStatus;
import com.skillbox.engine.model.enums.PostViewMode;
import com.skillbox.engine.repository.PostRepository;
import org.jsoup.Jsoup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.skillbox.engine.utilities.Utill.convertLongToLocalDateTime;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final Tag2PostService tag2PostService;
    private final PostCommentService postCommentService;
    private final HashMap<PostViewMode, SortModePost> sortModePost = new HashMap<>();
    private final HashMap<ModerationStatus, SortModeMyPost> sortModeMyPost = new HashMap<>();
    private final UserService userService;
    private final TagService tagService;

    public PostService(PostRepository postRepository,
                       Tag2PostService tag2PostService,
                       PostCommentService postCommentService,
                       UserService userService,
                       TagService tagService) {
        this.postRepository = postRepository;
        this.tag2PostService = tag2PostService;
        this.postCommentService = postCommentService;
        this.userService = userService;
        this.tagService = tagService;
        initSort();
    }

    public PostDetailResponse getPostById(int postId, String userEmail) throws NotFoundException {
        User user = null;
        Optional<Post> postResult;
        if (userEmail != null){
            user = userService.findByEmail(userEmail).orElseThrow(()-> new NotFoundException("user not found"));
            postResult = postRepository.findPostByIdAndIsActiveOrUserId(postId, user);
        }else{
            postResult = postRepository.findPostByIdAndIsActiveAndModerationStatus(postId);
        }
        Post post = postResult.orElseThrow(() -> new NotFoundException("Запись с id: " + postId + " не найдена"));
        List<Tag> tags = tag2PostService.getPostTags(postId);
        List<PostComment> postComments = postCommentService.getPostCommentsByPostId(postId);
        int viewCount = post.getViewCount();
        if (user!=null){
            if (!user.getModeration() && post.getUser()!=user){
                post.setViewCount(++viewCount);
            }
        }else {
            post.setViewCount(++viewCount);
        }
        postRepository.save(post);
        return buildPostDetailResponse(post, tags, postComments);
    }

    public Optional<Post> getById(int id){
        return postRepository.findPostByIdAndIsActiveAndModerationStatus(id);

    }

    public long numberOfPostsForMoreration() {
        return postRepository.countByModerationStatusNew();
    }

    public PostResponse getSearchPost(int offset, int limit, String query) {
        if (query.isBlank() || query.isEmpty()) {
            return getAllPosts(offset, limit, "recent");
        }
        PageRequest pageRequest = getPageRequest(offset, limit);
        Page<Post> posts = postRepository.SearchesByPostsSortByDateDesc(pageRequest, query.trim());
        List<PostDTO> postsDTO = getPostDTOS(posts);
        return getPostResponse(posts.getTotalElements(), postsDTO);
    }

    public PostResponse getAllPosts(int offset, int limit, String mode) {
        PostViewMode viewMode = PostViewMode.valueOf(mode.toUpperCase());
        PageRequest pageRequest = getPageRequest(offset, limit);
        Page<Post> posts = sortModePost.get(viewMode).sort(pageRequest);
        List<PostDTO> postsDTO = getPostDTOS(posts);
        return getPostResponse(posts.getTotalElements(), postsDTO);
    }

    public PostResponse getPostsModeration(int offset, int limit, String status, String userEmail) throws NotFoundException {

        PageRequest pageRequest = getPageRequest(offset, limit);
        PostModerationStatus moderationStatus = PostModerationStatus.valueOf(status.toUpperCase());
        Page<Post> posts;
        if (moderationStatus == PostModerationStatus.NEW){
            posts = postRepository.findPostsModerationStatusNew(pageRequest);
        }else{
            User currentUser = userService.findByEmail(userEmail)
                    .orElseThrow(()-> new NotFoundException("user not found"));
            posts = postRepository.findPostsModerationStatusNotNew(pageRequest, currentUser, moderationStatus);
        }
        List<PostDTO> postsDTO = getPostDTOS(posts);
        return getPostResponse(posts.getTotalElements(), postsDTO);
    }

    public PostResponse getMyPosts(int offset, int limit, String status, String userEmail) throws NotFoundException {
        User currentUser = userService.findByEmail(userEmail)
                .orElseThrow(()-> new NotFoundException("user not found"));
        ModerationStatus moderationStatus = ModerationStatus.valueOf(status.toUpperCase());
        PageRequest pageRequest = getPageRequest(offset, limit);
        Page<Post> posts = sortModeMyPost.get(moderationStatus).sort(pageRequest, currentUser.getId());
        List<PostDTO> postsDTO = getPostDTOS(posts);
        return getPostResponse(posts.getTotalElements(), postsDTO);
    }

    public CalendarResponse getCalendar(int year) {
        List<CalendarYearDTO> calendarYearDTOList = postRepository.getYearsOfPosts();
        List<CalendarDatePostCount> calendarDatePostCountList = postRepository.getTheCountOfPostsByDateOfPosts(year);

        return buildCalendarResponse(calendarYearDTOList, calendarDatePostCountList);
    }

    public PostResponse getAllPostsOnTheDate(int offSet, int limit, String date) {
        PageRequest pageRequest = getPageRequest(offSet, limit);
        Page<Post> posts = postRepository.findAllPostsOnTheDate(pageRequest, date);
        List<PostDTO> postsDTO = getPostDTOS(posts);
        return getPostResponse(posts.getTotalElements(), postsDTO);
    }

    public PostResponse getPostsByTag(int offSet, int limit, String tag) {
        PageRequest pageRequest = getPageRequest(offSet, limit);
        Page<Post> posts = postRepository.findPostsByTag(pageRequest, tag);
        List<PostDTO> postsDTO = getPostDTOS(posts);
        return getPostResponse(posts.getTotalElements(), postsDTO);
    }

    private PostDetailResponse buildPostDetailResponse(Post post, List<Tag> tags, List<PostComment> postComments) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .time(post.getTime().toInstant().getEpochSecond())
                .user(userService.buildUserDTO(post.getUser()))
                .title(post.getTitle())
                .text(post.getText())
                .likeCount(sumCountVotes(post.getPostVotes(), true))
                .active(post.getIsActive() == 1)
                .dislikeCount(sumCountVotes(post.getPostVotes(), false))
                .viewCount(post.getViewCount())
                .tags(getTags(tags))
                .comments(getComments(postComments))
                .build();
    }

    private Collection<CommentDTO> getComments(List<PostComment> postComments) {
        return postComments.stream()
                .map(postCommentService::buildCommentTDO)
                .collect(Collectors.toList());
    }

    private Collection<String> getTags(List<Tag> tags) {
        return tags.stream().map(Tag::getName).collect(Collectors.toList());
    }

    private CalendarResponse buildCalendarResponse(List<CalendarYearDTO> calendarYearDTOList,
                                                   List<CalendarDatePostCount> calendarDatePostCountList) {
        return CalendarResponse.builder()
                .years(calendarYearDTOList.stream()
                        .map(CalendarYearDTO::getYear)
                        .collect(Collectors.toList()))
                .posts(calendarDatePostCountList.stream()
                        .collect(Collectors.toMap(CalendarDatePostCount::getDate, CalendarDatePostCount::getCount)))
                .build();
    }

    private PostResponse getPostResponse(long countTotalElements, List<PostDTO> postsDTO) {
        return PostResponse.builder()
                .count(countTotalElements)
                .posts(postsDTO).build();
    }

    private List<PostDTO> getPostDTOS(Page<Post> posts) {
        return posts.getContent().stream()
                .map(this::buildPostDTO)
                .collect(Collectors.toList());
    }

    private PageRequest getPageRequest(int offset, int limit) {
        int numberPage = offset / limit;
        return PageRequest.of(numberPage, limit);
    }

    private void initSort() {
        sortModePost.put(PostViewMode.RECENT, postRepository::findPostsOrderByDateDesc);
        sortModePost.put(PostViewMode.POPULAR, postRepository::findPostsOrderByComment);
        sortModePost.put(PostViewMode.BEST, postRepository::findPostsOrderByLikes);
        sortModePost.put(PostViewMode.EARLY, postRepository::findPostsOrderByDateAsc);

        sortModeMyPost.put(ModerationStatus.INACTIVE, postRepository::findMyPostsInactiveOrderByDateDesc);
        sortModeMyPost.put(ModerationStatus.PENDING, postRepository::findMyPostsPendingOrderByDateDesc);
        sortModeMyPost.put(ModerationStatus.DECLINED, postRepository::findMyPostsDeclinedOrderByDateDesc);
        sortModeMyPost.put(ModerationStatus.PUBLISHED, postRepository::findMyPostsPublishedOrderByDateDesc);

    }

    private PostDTO buildPostDTO(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .time(post.getTime().toInstant().getEpochSecond())
                .user(userService.buildUserDTO(post.getUser()))
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
        return textWithoutHtml.length() > 150 ? textWithoutHtml.substring(0, 150) + "..." : textWithoutHtml + "...";
    }


    private int sumCountVotes(Collection<PostVotes> postVotes, boolean like) {
        if (like)
            return (int) postVotes.stream().filter(postVotes1 -> postVotes1.getValue() > 0).count();
        else
            return (int) postVotes.stream().filter(postVotes1 -> postVotes1.getValue() < 0).count();
    }

    public PostUpdateResponse addPost(String userEmail, PostRequest postRequest) throws NotFoundException {
        PostUpdateResponse postUpdateResponse = new PostUpdateResponse();
        ErrorUpdatePost errorUpdatePost = new ErrorUpdatePost();
        boolean isError = isError(postRequest, errorUpdatePost);
        if (isError){
            postUpdateResponse.setResult(false);
            postUpdateResponse.setErrors(errorUpdatePost);
            return postUpdateResponse;
        }
        postUpdateResponse.setResult(true);
        Post post = createOrUpdatePost(userEmail, postRequest, new Post());
        postRepository.save(post);
        return  postUpdateResponse;
    }

    public PostUpdateResponse editPost(String userEmail, PostRequest postRequest, int id) throws NotFoundException {
        PostUpdateResponse postUpdateResponse = new PostUpdateResponse();
        ErrorUpdatePost errorUpdatePost = new ErrorUpdatePost();
        boolean isError = isError(postRequest, errorUpdatePost);
        if (isError){
            postUpdateResponse.setResult(false);
            postUpdateResponse.setErrors(errorUpdatePost);
            return postUpdateResponse;
        }
        postUpdateResponse.setResult(true);
        Post currentPost = postRepository.findById(id).orElseThrow(()->new NotFoundException("post not found"));
        Post updatePost = createOrUpdatePost(userEmail, postRequest, currentPost);
        postRepository.save(updatePost);
        return  postUpdateResponse;
    }

    private Post createOrUpdatePost(String userEmail, PostRequest postRequest, Post post) throws NotFoundException {
        User currentUser = userService.findByEmail(userEmail).orElseThrow(()-> new NotFoundException("user not found"));
        post.setIsActive(postRequest.getActive());
        if (!currentUser.getModeration()){
            post.setModerationStatus(PostModerationStatus.NEW);
        }
        post.setTime(Timestamp.valueOf(convertLongToLocalDateTime(postRequest.getTimestamp())));
        post.setUser(currentUser);
        post.setTitle(postRequest.getTitle());
        post.setText(postRequest.getText());
        Collection<Tag> tags = getTags(postRequest);
        post.setTag(tags);
        return post;
    }

//    private LocalDateTime convertLongToLocalDateTime(Long longTime) {
//        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochSecond(longTime),
//                TimeZone.getDefault().toZoneId());
//        if (time.compareTo(LocalDateTime.now())<1){
//            time = LocalDateTime.now();
//        }
//        return time;
//    }

    private Collection<Tag> getTags(PostRequest postRequest) {
        Collection<Tag> tags = new ArrayList<>();
        for (String currentTag : postRequest.getTags()) {
            tags.add(tagService.addIfNot(currentTag));
        }
        return tags;
    }

    private boolean isError(PostRequest postRequest, ErrorUpdatePost errorUpdatePost) {
        boolean isError=false;
        if (postRequest.getTitle().length()<3){
            isError = true;
            errorUpdatePost.setTitle("Заголовок не установлен");
        }
        if (postRequest.getText().length()<50){
            isError = true;
            errorUpdatePost.setTitle("Текст публикации слишком короткий");
        }
        return isError;
    }

}
