package com.bizarreDomain.RedditClone.repository;

import com.bizarreDomain.RedditClone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
