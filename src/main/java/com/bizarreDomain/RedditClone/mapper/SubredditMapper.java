package com.bizarreDomain.RedditClone.mapper;

import com.bizarreDomain.RedditClone.dto.SubredditDTO;
import com.bizarreDomain.RedditClone.model.Post;
import com.bizarreDomain.RedditClone.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPostsCount(subreddit.getPosts()))")
    SubredditDTO mapSubredditToDto(Subreddit subreddit);

    default Integer mapPostsCount(List<Post> numberOfPosts){
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDTO subredditDTO);
}
