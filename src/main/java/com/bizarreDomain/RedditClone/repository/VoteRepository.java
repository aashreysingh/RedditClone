package com.bizarreDomain.RedditClone.repository;

import com.bizarreDomain.RedditClone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
