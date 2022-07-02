package com.bizarreDomain.RedditClone.service;

import com.bizarreDomain.RedditClone.exceptions.SpringRedditException;
import com.bizarreDomain.RedditClone.model.RefreshToken;
import com.bizarreDomain.RedditClone.model.User;
import com.bizarreDomain.RedditClone.repository.RefreshTokenRepository;
import com.bizarreDomain.RedditClone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(String username){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        refreshToken.setUser(user);
        return refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshToken(String token, String username){
        User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        refreshTokenRepository.findByTokenAndUser(token, user)
                .orElseThrow(() -> new SpringRedditException("Invalid Refresh Token!"));
    }

    public void deleteRefreshToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }
}

