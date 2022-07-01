package com.bizarreDomain.RedditClone.mapper;

import com.bizarreDomain.RedditClone.dto.VoteDTO;
import com.bizarreDomain.RedditClone.model.Post;
import com.bizarreDomain.RedditClone.model.User;
import com.bizarreDomain.RedditClone.model.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoteMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    Vote voteDtoToVote(VoteDTO voteDTO, User user, Post post);

    @Mapping(target = "postId", source = "vote.post.postId")
    VoteDTO voteToDTO(Vote vote);

}
