package com.bizarreDomain.RedditClone.service;

import com.bizarreDomain.RedditClone.dto.request.CommentRequest;
import com.bizarreDomain.RedditClone.dto.response.CommentResponse;
import com.bizarreDomain.RedditClone.exceptions.PostNotFoundException;
import com.bizarreDomain.RedditClone.mapper.CommentMapper;
import com.bizarreDomain.RedditClone.model.Comment;
import com.bizarreDomain.RedditClone.model.NotificationEmail;
import com.bizarreDomain.RedditClone.model.Post;
import com.bizarreDomain.RedditClone.model.User;
import com.bizarreDomain.RedditClone.repository.CommentRepository;
import com.bizarreDomain.RedditClone.repository.PostRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final CommentMapper commentMapper;

    private final AuthService authService;
    private final MailService mailService;

    public CommentResponse createComment(CommentRequest commentRequest){
        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID:" + commentRequest.getPostId()));
        User user = authService.getCurrentUser();
        Comment comment = commentMapper.commentRequestToComment(commentRequest, post, user);
        Comment savedComment = commentRepository.save(comment);

        mailService.sendMail(new NotificationEmail("u/"+user.getUsername()+" commented on your post!",
                post.getUser().getEmail(),
                "u/"+user.getUsername()+" has commented on your post. \nTo view the comment please launch reddit application."));
        return commentMapper.commentToCommentResponse(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getAllCommentsForPosts(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with the provided ID not found!"));
        return commentRepository.findAllByPost(post)
                .stream()
                .map(commentMapper::commentToCommentResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByUsername(String userName){
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User with the provided UserName not found!"));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::commentToCommentResponse)
                .collect(Collectors.toList());
    }
}
