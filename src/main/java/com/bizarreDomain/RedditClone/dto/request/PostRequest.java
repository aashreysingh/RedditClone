package com.bizarreDomain.RedditClone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PostRequest {
    private Long postId;
    private String subredditName;
    private String postName;
    private String url;
    private String description;
}
