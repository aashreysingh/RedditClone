package com.bizarreDomain.RedditClone.controller;

import com.bizarreDomain.RedditClone.dto.request.PostRequest;
import com.bizarreDomain.RedditClone.dto.response.PostResponse;
import com.bizarreDomain.RedditClone.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.save(postRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getPost(postId));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return ResponseEntity.status(HttpStatus.OK)
                        .body(postService.getAllPosts());
    }

    @GetMapping("/by-subreddit/{subredditId}")
    public ResponseEntity<List<PostResponse>> getPostBySubreddit(@PathVariable Long subredditId){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsBySubreddit(subredditId));
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String userName){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsByUsername(userName));
    }
}
