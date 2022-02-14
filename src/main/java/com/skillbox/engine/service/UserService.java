package com.skillbox.engine.service;

import com.skillbox.engine.api.request.UserRegistrRequest;
import com.skillbox.engine.model.DTO.UserDTO;
import com.skillbox.engine.model.DTO.UserPhotoDTO;
import com.skillbox.engine.model.entity.User;
import com.skillbox.engine.repository.UserRepository;
import com.skillbox.engine.security.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Util util;

    public long findCountEmail(String email) {
        return userRepository.countByEmailEquals(email);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public void addUser(UserRegistrRequest userRegistrRequest) {
        User user = new User();
        user.setName(userRegistrRequest.getName());
        user.setEmail(userRegistrRequest.getEmail());
        user.setPassword(util.passwordEncoder().encode(userRegistrRequest.getPassword()));
        user.setRegTime(LocalDateTime.now());
        userRepository.save(user);
    }

    public UserDTO buildUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public UserPhotoDTO buildUserPhotoDTO(User user) {
        return UserPhotoDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .photo(user.getPhoto())
                .build();
    }
}
