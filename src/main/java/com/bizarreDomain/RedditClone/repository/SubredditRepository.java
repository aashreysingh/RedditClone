package com.bizarreDomain.RedditClone.repository;

import com.bizarreDomain.RedditClone.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
}
