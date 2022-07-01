package com.bizarreDomain.RedditClone.mapper;

import com.bizarreDomain.RedditClone.dto.request.PostRequest;
import com.bizarreDomain.RedditClone.dto.response.PostResponse;
import com.bizarreDomain.RedditClone.model.Post;
import com.bizarreDomain.RedditClone.model.Subreddit;
import com.bizarreDomain.RedditClone.model.User;
import com.bizarreDomain.RedditClone.repository.CommentRepository;
import com.bizarreDomain.RedditClone.repository.VoteRepository;
import com.bizarreDomain.RedditClone.service.AuthService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "voteCount", constant = "0")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "commentCount", expression = "java(getCommentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse mapToPostResponse(Post post);

    Integer getCommentCount(Post post){
        return commentRepository.findAllByPost(post).size();
    }

    String getDuration(Post post){
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

}
