package com.bitsu.social_media.controller;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bitsu.social_media.dto.AuthenticationResponse;
import com.bitsu.social_media.dto.LoginRequest;
import com.bitsu.social_media.dto.RegisterRequest;
import com.bitsu.social_media.model.Role;
import com.bitsu.social_media.model.User;
import com.bitsu.social_media.repository.UserRepo;
import com.bitsu.social_media.service.AuthService;
import com.bitsu.social_media.service.JwtService;
import jakarta.xml.bind.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
public class AuthControllerTest {
    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthService authService;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;
    @Mock
    private AuthController authController;
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .build();

        registerRequest = new RegisterRequest("John", "Doe", "johndoe", "password123");
        loginRequest = new LoginRequest("johndoe", "password123");
        user = User.builder()
                .id(1)
                .firstname("John")
                .lastname("Doe")
                .username("johndoe")
                .password("encodedPassword")
                .bio("Bio")
                .profilePicture("profilePic")
                .role(Role.USER)
                .build();
    }

    @Test
    void testRegister() {
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        AuthenticationResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(userRepo).save(any(User.class));
    }

    @Test
    void testLogin() {
        when(userRepo.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
//        doNothing().when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", "1");
        claims.put("profilePicture", "profilePic");
        claims.put("bio", "Bio");
        claims.put("firstname", "John");
        claims.put("lastname", "Doe");
        when(jwtService.generateToken(claims, user)).thenReturn("jwtToken");

        AuthenticationResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepo).findByUsername(loginRequest.getUsername());
    }

    @Test
    void testLoginThrowsExceptionWhenUserNotFound() {
        when(userRepo.findByUsername(loginRequest.getUsername())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("johndoe", exception.getMessage());
        verify(userRepo).findByUsername(loginRequest.getUsername());
    }

    @Test
    void testLoginWithInvalidFields() throws Exception {
        LoginRequest invalidRequest = new LoginRequest("", "");


        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("username", containsString("Username is required")))
                .andExpect(jsonPath("password", containsString("Password is required")));
    }
}
