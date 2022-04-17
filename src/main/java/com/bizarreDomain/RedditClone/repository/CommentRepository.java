package com.bizarreDomain.RedditClone.repository;

import com.bizarreDomain.RedditClone.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
