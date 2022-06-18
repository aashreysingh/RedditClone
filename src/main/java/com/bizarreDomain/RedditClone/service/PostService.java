package com.bizarreDomain.RedditClone.service;

import com.bizarreDomain.RedditClone.dto.request.PostRequest;
import com.bizarreDomain.RedditClone.dto.response.PostResponse;
import com.bizarreDomain.RedditClone.exceptions.PostNotFoundException;
import com.bizarreDomain.RedditClone.exceptions.SubredditNotFoundException;
import com.bizarreDomain.RedditClone.mapper.PostMapper;
import com.bizarreDomain.RedditClone.model.Post;
import com.bizarreDomain.RedditClone.model.Subreddit;
import com.bizarreDomain.RedditClone.model.User;
import com.bizarreDomain.RedditClone.repository.PostRepository;
import com.bizarreDomain.RedditClone.repository.SubredditRepository;
import com.bizarreDomain.RedditClone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    public PostResponse save(PostRequest postRequest){
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                        .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName() + "not Found!"));
        User currentUser = authService.getCurrentUser();
        Post post = postRepository.save(postMapper.map(postRequest, subreddit, currentUser));
        return postMapper.mapToPostResponse(post);
    }

    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id - " + postId));

        return postMapper.mapToPostResponse(post);
    }

    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(postMapper::mapToPostResponse).collect(Collectors.toList());
    }

    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToPostResponse).collect(Collectors.toList());

    }

    public List<PostResponse> getPostsByUsername(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        List<Post> posts = postRepository.findAllByUser(user);
        return posts.stream().map(postMapper::mapToPostResponse).collect(Collectors.toList());
    }
}
