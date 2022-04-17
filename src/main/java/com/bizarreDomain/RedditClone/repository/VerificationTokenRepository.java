package com.bizarreDomain.RedditClone.repository;

import com.bizarreDomain.RedditClone.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
}
