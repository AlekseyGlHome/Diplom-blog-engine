package com.skillbox.engine.model.entity;

import com.skillbox.engine.model.enums.PostModerationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "is_active", nullable = false, columnDefinition = "tinyint")
    private byte isActive;
    @Column(name = "moderation_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostModerationStatus moderationStatus = PostModerationStatus.NEW;
    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private User moderator;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "time")
    private LocalDateTime time;
    @Column(name = "title")
    private String title;
    @Column(name = "text", columnDefinition = "TEXT")
    private String text;
    @Column(name = "view_count")
    private int viewCount;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tag2post",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Collection<Tag> tag = new ArrayList<>();
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private Collection<PostComment> postComments = new ArrayList<>();

}
