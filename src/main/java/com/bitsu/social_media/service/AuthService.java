package com.bitsu.social_media.service;

import com.bitsu.social_media.dto.AuthenticationResponse;
import com.bitsu.social_media.dto.LoginRequest;
import com.bitsu.social_media.dto.RegisterRequest;
import com.bitsu.social_media.model.Role;
import com.bitsu.social_media.model.User;
import com.bitsu.social_media.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        user = userRepo.save(user);

        var token = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(request.getUsername()));

        Map<String, Object> extraClaims = new HashMap<String, Object>();
        extraClaims.put("id", Integer.toString(user.getId()));
        extraClaims.put("profilePicture", user.getProfilePicture());
        extraClaims.put("bio", user.getBio());
        extraClaims.put("firstname", user.getFirstname());
        extraClaims.put("lastname", user.getLastname());

        var token = jwtService.generateToken(extraClaims, user);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
