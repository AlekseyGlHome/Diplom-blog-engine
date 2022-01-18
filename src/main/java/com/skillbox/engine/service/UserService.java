package com.skillbox.engine.service;

import com.skillbox.engine.api.request.UserRequest;
import com.skillbox.engine.model.DTO.UserDTO;
import com.skillbox.engine.model.DTO.UserPhotoDTO;
import com.skillbox.engine.model.entity.User;
import com.skillbox.engine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository userRepository;

    public long findEmail(String email){
        return userRepository.countByEmailEquals(email);
    }

    public void addUser(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setEMail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
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
