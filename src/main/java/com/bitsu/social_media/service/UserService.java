package com.bitsu.social_media.service;

import com.bitsu.social_media.dto.UserBioInfo;
import com.bitsu.social_media.dto.UserPIInfo;
import com.bitsu.social_media.dto.UserProfilePic;
import com.bitsu.social_media.dto.UserProfileResponse;
import com.bitsu.social_media.dto.UserResponse;
import com.bitsu.social_media.model.Post;
import com.bitsu.social_media.model.User;
import com.bitsu.social_media.repository.UserRepo;
import com.bitsu.social_media.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final S3Service s3Service;
    private final Utility utility;

    public UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .Id(user.getId())
                .username(user.getUsername())
                .bio(user.getBio())
                .profilePicture(user.getProfilePicture())
                .build();
    }

    public void updatePI(UserPIInfo userPIInfo) {
        User user = utility.getLoggedInUser();
        user.setFirstname(userPIInfo.getFirstname());
        user.setLastname(userPIInfo.getLastname());
        userRepo.save(user);
    }

    public void updateBio(UserBioInfo userBioInfo) {
        User user = utility.getLoggedInUser();
        user.setBio(userBioInfo.getBio());
        userRepo.save(user);
    }

    public void updateProfilePic(UserProfilePic userProfilePic) {
        User user = utility.getLoggedInUser();
        if (s3Service.deleteImageFromBucket(user.getProfilePicture())) {
            user.setProfilePicture(userProfilePic.getProfilePicture());
            userRepo.save(user);
        }
    }

    public UserProfileResponse getUser(String username) {
        User user = userRepo.findByUsername(username).get();
        List<Post> posts = user.getPosts();
        Collections.reverse(posts);
        return UserProfileResponse.builder()
            .id(user.getId())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .username(user.getUsername())
            .bio(user.getBio())
            .profilePicture(user.getProfilePicture())
            .posts(posts)
            .build();
    }

    public List<UserResponse> getUsers(String search) {
        if (search == null || search.isBlank()) {
            return userRepo.findAll().stream()
                    .map(this::mapToUserResponse)
                    .toList();
        }
        return userRepo.findAllByUsernameContains(search).stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    public List<UserResponse> getFollowing(String search) {
        if (search == null || search.isBlank()) {
            return utility.getLoggedInUser().getFollowing().stream()
                    .map(this::mapToUserResponse)
                    .toList();
        }
        return utility.getLoggedInUser().getFollowing().stream()
                .filter(user -> user.getUsername().contains(search))
                .map(this::mapToUserResponse)
                .toList();
    }

    public List<UserResponse> getFollowers(String search) {
        if (search == null || search.isBlank()) {
            return userRepo.findFollowers(utility.getLoggedInUser().getId()).stream()
                .map(this::mapToUserResponse)
                .toList();
        }
        return userRepo.findFollowers(utility.getLoggedInUser().getId()).stream()
                .filter(user -> user.getUsername().contains(search))
                .map(this::mapToUserResponse)
                .toList();
    }

    public void unfollow(int id) {
        User user = utility.getLoggedInUser();
        User userToUnfollow = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User to unfollow not found"));
        user.getFollowing().remove(userToUnfollow);
        userRepo.save(user);
    }

    public void follow(int id) {
        User user = utility.getLoggedInUser();
        User userToFollow = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User to follow not found"));
        user.getFollowing().add(userToFollow);
        userRepo.save(user);
    }
}
