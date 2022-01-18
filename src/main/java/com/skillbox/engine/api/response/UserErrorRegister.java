package com.skillbox.engine.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserErrorRegister {
    private String email;
    private String name;
    private String password;
    private String captcha;

}
