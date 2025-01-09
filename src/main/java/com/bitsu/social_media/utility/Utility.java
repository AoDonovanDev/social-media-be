package com.bitsu.social_media.utility;

import com.bitsu.social_media.dto.CommentResponse;
import com.bitsu.social_media.dto.PostResponse;
import com.bitsu.social_media.dto.UserResponse;
import com.bitsu.social_media.model.Comment;
import com.bitsu.social_media.model.Post;
import com.bitsu.social_media.model.Reaction;
import com.bitsu.social_media.model.User;
import com.bitsu.social_media.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class Utility {
    private final UserRepo userRepo;

    public User getLoggedInUser() {
        return userRepo.findByUsername(
                ((User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal()
                ).getUsername()
        ).orElseThrow(() -> new RuntimeException("User not found ** change for testing webhook **"));
    }

    public UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .Id(user.getId())
                .username(user.getUsername())
                .bio(user.getBio())
                .profilePicture(user.getProfilePicture())
                .build();
    }

    public PostResponse mapToPostResponse(Post post) {

        List<Comment> comments = post.getComments();
        List<Reaction> reactions = post.getReactions();
        if (comments == null) {
            comments = List.of();
        }
        if (reactions == null) {
            reactions = List.of();
        }

        return PostResponse.builder()
                .Id(post.getId())
                .content(post.getContent())
                .mediaURL(post.getMediaURL())
                .createdAt(post.getCreatedAt())
                .user(
                        mapToUserResponse(
                                post.getUser()))
                .comments(
                        comments.stream().map(this::mapToCommentResponse).toList())
                .reactions(
                        reactions.stream().map(reaction -> reaction.getUser().getId()).toList())
                .build();
    }

    public CommentResponse mapToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .Id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .user(
                        mapToUserResponse(
                                comment.getUser()
                        )
                )
                .build();
    }

}
