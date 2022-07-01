package com.bizarreDomain.RedditClone.repository;

import com.bizarreDomain.RedditClone.model.Post;
import com.bizarreDomain.RedditClone.model.User;
import com.bizarreDomain.RedditClone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User user);

}
