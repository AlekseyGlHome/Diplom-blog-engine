package com.skillbox.engine.api.response;

import com.skillbox.engine.model.DTO.TagDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class TagResponse {

    Collection<TagDTO> tags = new ArrayList<>();
}
