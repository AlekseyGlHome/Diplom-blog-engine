package com.skillbox.engine.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface TagResponseRepository {
    String getName();

    int getCount();

}
