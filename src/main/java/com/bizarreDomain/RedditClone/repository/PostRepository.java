package com.bizarreDomain.RedditClone.repository;

import com.bizarreDomain.RedditClone.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
