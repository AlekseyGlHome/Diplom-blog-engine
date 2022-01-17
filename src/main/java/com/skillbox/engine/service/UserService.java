package com.skillbox.engine.service;

import com.skillbox.engine.model.DTO.UserDTO;
import com.skillbox.engine.model.DTO.UserPhotoDTO;
import com.skillbox.engine.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
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
