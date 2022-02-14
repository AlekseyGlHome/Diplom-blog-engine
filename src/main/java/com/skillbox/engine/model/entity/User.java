package com.skillbox.engine.model.entity;

import com.skillbox.engine.security.Role;
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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "is_moderator", nullable = false, columnDefinition = "tinyint")
    private byte isModerator;
    @Column(name = "reg_time", nullable = false)
    private LocalDateTime regTime;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "code")
    private String code;
    @Column(name = "photo", columnDefinition = "TEXT")
    private String photo;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<Post> posts = new ArrayList<>();

    public boolean getModeration(){
        return isModerator == 1;
    }

    public Role getRole() {
        return isModerator == 1 ? Role.MODERATOR : Role.USER;
    }
}
