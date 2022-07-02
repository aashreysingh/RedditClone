package com.bizarreDomain.RedditClone.repository;

import com.bizarreDomain.RedditClone.model.RefreshToken;
import com.bizarreDomain.RedditClone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByTokenAndUser(String token, User user);

    void deleteByToken(String token);
}
