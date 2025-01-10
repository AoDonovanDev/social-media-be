package com.bitsu.social_media.service;

import com.bitsu.social_media.dto.*;
import com.bitsu.social_media.exception.NotFoundException;
import com.bitsu.social_media.model.Post;
import com.bitsu.social_media.model.User;
import com.bitsu.social_media.repository.UserRepo;
import com.bitsu.social_media.utility.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepo userRepo;
    private final S3Service s3Service;
    private final Utility utility;
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
        if(userProfilePic.getProfilePicture() != null && !userProfilePic.getProfilePicture().isBlank()){
            s3Service.deleteImageFromBucket(user.getProfilePicture());
            user.setProfilePicture(userProfilePic.getProfilePicture());
        }
        userRepo.save(user);
    }

    public UserProfileResponse getUser(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        List<Post> posts = user.getPosts();
        Collections.reverse(posts);
        return UserProfileResponse.builder()
            .id(user.getId())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .username(user.getUsername())
            .bio(user.getBio())
            .profilePicture(user.getProfilePicture())
            .posts(posts.stream().map(utility::mapToPostResponse).toList())
            .build();
    }

    public PagedUser getUsers(String search, int page, int size, String sortBy, boolean ascending) {
        var loggedInUser = utility.getLoggedInUser();

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (search == null || search.isBlank()) {
            var usersPageable = userRepo.findAllByUsernameNot(loggedInUser.getUsername(), pageable);
            return PagedUser.builder()
                    .users(usersPageable.getContent().stream().map(utility::mapToUserResponse).toList())
                    .hasNext(usersPageable.hasNext())
                    .currentPage(usersPageable.getNumber())
                    .totalPages(usersPageable.getTotalPages())
                    .size(usersPageable.getSize())
                    .build();
        }
        var usersPageable = userRepo.findAllByUsernameContainsAndUsernameNot(search, loggedInUser.getUsername(), pageable);
        return PagedUser.builder()
                .users(usersPageable.getContent().stream().map(utility::mapToUserResponse).toList())
                .hasNext(usersPageable.hasNext())
                .currentPage(usersPageable.getNumber())
                .totalPages(usersPageable.getTotalPages())
                .size(usersPageable.getSize())
                .build();
    }

    public PagedUser getFollowing(String search, int page, int size, String sortBy, boolean ascending) {

        var loggedInUser = utility.getLoggedInUser();

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (search == null || search.isBlank()) {
            var usersPageable = userRepo.findAllFollowingUser(loggedInUser, pageable);
            return PagedUser.builder()
                    .users(usersPageable.getContent().stream().map(utility::mapToUserResponse).toList())
                    .hasNext(usersPageable.hasNext())
                    .currentPage(usersPageable.getNumber())
                    .totalPages(usersPageable.getTotalPages())
                    .size(usersPageable.getSize())
                    .build();
        }
        var usersPageable = userRepo.findAllByFollowingAndUsernameContains(loggedInUser, search, pageable);
        return PagedUser.builder()
                .users(usersPageable.getContent().stream().map(utility::mapToUserResponse).toList())
                .hasNext(usersPageable.hasNext())
                .currentPage(usersPageable.getNumber())
                .totalPages(usersPageable.getTotalPages())
                .size(usersPageable.getSize())
                .build();
    }

    public PagedUser getFollowers(String search, int page, int size, String sortBy, boolean ascending) {

        var loggedInUser = utility.getLoggedInUser();

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (search == null || search.isBlank()) {

            var usersPageable = userRepo.findFollowers(loggedInUser.getId(), pageable);
            return PagedUser.builder()
                    .users(usersPageable.getContent().stream().map(utility::mapToUserResponse).toList())
                    .hasNext(usersPageable.hasNext())
                    .currentPage(usersPageable.getNumber())
                    .totalPages(usersPageable.getTotalPages())
                    .size(usersPageable.getSize())
                    .build();
        }

        var usersPageable = userRepo.findFollowers(loggedInUser.getId(), search, pageable);
        return PagedUser.builder()
                .users(usersPageable.getContent().stream().map(utility::mapToUserResponse).toList())
                .hasNext(usersPageable.hasNext())
                .currentPage(usersPageable.getNumber())
                .totalPages(usersPageable.getTotalPages())
                .size(usersPageable.getSize())
                .build();
    }

    public void unfollow(int id) {
        log.info("here");
        User user = utility.getLoggedInUser();
        User userToUnfollow = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User to unfollow not found"));
        user.getFollowing().remove(userToUnfollow);
        userRepo.save(user);
    }

    public void follow(int id) {
        User user = utility.getLoggedInUser();
        User userToFollow = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User to follow not found"));
        user.getFollowing().add(userToFollow);
        userRepo.save(user);
    }

    public boolean checkFollowing(int id) {
        User user = utility.getLoggedInUser();
        User userToCheck = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User to check not found"));
        var res = user.getFollowing().contains(userToCheck);
        log.info("Following: " + res);
        return res;
    }
}
