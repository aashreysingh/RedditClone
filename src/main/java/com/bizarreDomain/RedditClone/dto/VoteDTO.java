package com.bizarreDomain.RedditClone.dto;

import com.bizarreDomain.RedditClone.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDTO {
    private VoteType voteType;
    private Long postId;
}
