package com.bizarreDomain.RedditClone.service;

import com.bizarreDomain.RedditClone.dto.request.LoginRequest;
import com.bizarreDomain.RedditClone.dto.request.RefreshTokenRequest;
import com.bizarreDomain.RedditClone.dto.request.RegisterRequest;
import com.bizarreDomain.RedditClone.dto.response.AuthenticationResponse;
import com.bizarreDomain.RedditClone.exceptions.SpringRedditException;
import com.bizarreDomain.RedditClone.model.NotificationEmail;
import com.bizarreDomain.RedditClone.model.User;
import com.bizarreDomain.RedditClone.model.VerificationToken;
import com.bizarreDomain.RedditClone.repository.UserRepository;
import com.bizarreDomain.RedditClone.repository.VerificationTokenRepository;
import com.bizarreDomain.RedditClone.security.JWTProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        if(checkIfUserAlreadyExists(registerRequest)){
            throw new SpringRedditException("User with the email/username already exists. Please try with different username/email.");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedDate(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account.",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account: " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    @Transactional(readOnly = true)
    public User getCurrentUser(){
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found - " + principal.getSubject()));
    }

    // persisting token against user in DB so that later we can perform token look up in DB and enable the specific user to which token belongs
    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    private boolean checkIfUserAlreadyExists(RegisterRequest registerRequest){
        return (userRepository.findByUsername(registerRequest.getUsername()).isPresent() || userRepository.findByEmail(registerRequest.getEmail()).isPresent());
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token!"));
        fetchUserAndEnable(verificationToken.get());
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with name - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    // authenticating using UsernamePasswordAuthenticationToken (JWT Authentication)
    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        // storing Authentication object inside SecurityContextHolder
        // to check whether the user is logged in or not, we will check Authentication object is present in our SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // generating JWT token and send the token to client
        String token = jwtProvider.generateToken(authentication);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .username(loginRequest.getUsername())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .refreshToken(refreshTokenService.generateRefreshToken(loginRequest.getUsername()).getToken())
                .build();

    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken(), refreshTokenRequest.getUsername());
        String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .username(refreshTokenRequest.getUsername())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .build();

    }
}
