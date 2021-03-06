package com.bizarreDomain.RedditClone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private Long id;
    private Long postId;
    private Instant createdDate;
    private String text;
    private String userName;
}
