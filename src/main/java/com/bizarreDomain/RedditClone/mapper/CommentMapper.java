package com.bizarreDomain.RedditClone.mapper;

import com.bizarreDomain.RedditClone.dto.request.CommentRequest;
import com.bizarreDomain.RedditClone.dto.response.CommentResponse;
import com.bizarreDomain.RedditClone.model.Comment;
import com.bizarreDomain.RedditClone.model.Post;
import com.bizarreDomain.RedditClone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment commentRequestToComment(CommentRequest commentRequest, Post post, User user);

    @Mapping(target = "postId", source = "post.postId")
    @Mapping(target = "userName", source = "user.username")
    CommentResponse commentToCommentResponse(Comment comment);
}
