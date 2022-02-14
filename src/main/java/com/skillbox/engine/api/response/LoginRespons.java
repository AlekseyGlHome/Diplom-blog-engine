package com.skillbox.engine.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRespons {
    private boolean result;
    @JsonProperty("user")
    private UserLoginRespons userLoginRespons;
}
