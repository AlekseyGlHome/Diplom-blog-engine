package com.skillbox.engine.api.response;

import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarResponse {
    Collection<Integer> years = new HashSet<>();
    Map<String,Integer> posts = new HashMap<>();
}
