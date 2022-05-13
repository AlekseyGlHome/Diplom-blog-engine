package com.skillbox.engine.model.DTO;

import lombok.Data;

@Data
public class ErrorUserUpdate {
    private String email;
    private String photo;
    private String name;
    private String password;
}
