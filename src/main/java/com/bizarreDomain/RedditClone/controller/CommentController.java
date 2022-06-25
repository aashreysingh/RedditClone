package com.bizarreDomain.RedditClone.controller;

import com.bizarreDomain.RedditClone.dto.request.CommentRequest;
import com.bizarreDomain.RedditClone.dto.response.CommentResponse;
import com.bizarreDomain.RedditClone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComments(@RequestBody CommentRequest commentRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(commentRequest));
    }

    @GetMapping("/by-postId/{postId}")
    public ResponseEntity<List<CommentResponse>> getAllCommentsForPost(@PathVariable Long postId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllCommentsForPosts(postId));
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentResponse>> getCommentsByUsername(@PathVariable String userName){
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getCommentsByUsername(userName));
    }


}
