package com.bizarreDomain.RedditClone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubredditDTO {

    private Long subredditId;
    private String name;
    private String description;
    private Integer numberOfPosts;

}
