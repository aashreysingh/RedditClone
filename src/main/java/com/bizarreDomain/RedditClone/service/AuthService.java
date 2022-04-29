package com.bizarreDomain.RedditClone.service;

import com.bizarreDomain.RedditClone.dto.AuthenticationResponse;
import com.bizarreDomain.RedditClone.dto.LoginRequest;
import com.bizarreDomain.RedditClone.dto.RegisterRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest registerRequest){
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

    // persisting token against user in DB so that later we can perform token look up in DB and enable the specific user to which token belongs
    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token!"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
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
        return new AuthenticationResponse(token, loginRequest.getUsername());

    }
}
