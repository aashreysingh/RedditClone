package com.bizarreDomain.RedditClone.mapper;

import com.bizarreDomain.RedditClone.dto.request.PostRequest;
import com.bizarreDomain.RedditClone.dto.response.PostResponse;
import com.bizarreDomain.RedditClone.model.Post;
import com.bizarreDomain.RedditClone.model.Subreddit;
import com.bizarreDomain.RedditClone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "subredditName", source = "subreddit.name")
    PostResponse mapToPostResponse(Post post);

}
