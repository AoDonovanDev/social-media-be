package com.bitsu.social_media.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bitsu.social_media.dto.UserBioInfo;
import com.bitsu.social_media.dto.UserPIInfo;
import com.bitsu.social_media.dto.UserProfilePic;
import com.bitsu.social_media.dto.UserResponse;
import com.bitsu.social_media.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.List;
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserResponse userResponse;
    private UserPIInfo userPIInfo;
    private UserBioInfo userBioInfo;
    private UserProfilePic userProfilePic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userResponse = UserResponse.builder()
                .Id(1)
                .username("testuser")
                .bio("Sample bio")
                .profilePicture("http://example.com/profile.jpg")
                .build();

        userPIInfo = UserPIInfo.builder()
                .firstname("John")
                .lastname("Doe")
                .build();

        userBioInfo = UserBioInfo.builder()
                .bio("Updated bio")
                .build();

        userProfilePic = UserProfilePic.builder()
                .profilePicture("http://example.com/newprofile.jpg")
                .build();
    }

//    @Test
//    void testGetUsers() {
//        when(userService.getUsers("test")).thenReturn(List.of(userResponse));
//
//        ResponseEntity<List<UserResponse>> response = userController.getUsers("test");
//
//        assertNotNull(response);
//        assertEquals(1, response.getBody().size());
//        verify(userService).getUsers("test");
//    }

//    @Test
//    void testGetFollowing() {
//        when(userService.getFollowing("test")).thenReturn(List.of(userResponse));
//
//        ResponseEntity<List<UserResponse>> response = userController.getFollowing("test");
//
//        assertNotNull(response);
//        assertEquals(1, response.getBody().size());
//        verify(userService).getFollowing("test");
//    }

    @Test
    void testUpdatePI() {
        doNothing().when(userService).updatePI(userPIInfo);

        userController.updatePI(userPIInfo);

        verify(userService).updatePI(userPIInfo);
    }

    @Test
    void testUpdateBio() {
        doNothing().when(userService).updateBio(userBioInfo);

        userController.updateBio(userBioInfo);

        verify(userService).updateBio(userBioInfo);
    }

    @Test
    void testUpdateProfilePic() {
        doNothing().when(userService).updateProfilePic(userProfilePic);

        userController.updateProfilePic(userProfilePic);

        verify(userService).updateProfilePic(userProfilePic);
    }

    @Test
    void testUnfollow() {
        doNothing().when(userService).unfollow(1);

        userController.unfollow(1);

        verify(userService).unfollow(1);
    }

    @Test
    void testFollow() {
        doNothing().when(userService).follow(1);

        userController.follow(1);

        verify(userService).follow(1);
    }
}
