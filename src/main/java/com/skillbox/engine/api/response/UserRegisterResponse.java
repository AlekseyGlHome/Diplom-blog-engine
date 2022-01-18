package com.skillbox.engine.api.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterResponse {
    private boolean result;
    private UserErrorRegister errors;
}
