package com.bitsu.social_media.service;

import com.bitsu.social_media.dto.PostRequest;
import com.bitsu.social_media.dto.PostResponse;
import com.bitsu.social_media.exception.NotFoundException;
import com.bitsu.social_media.model.Post;
import com.bitsu.social_media.model.Reaction;
import com.bitsu.social_media.model.User;
import com.bitsu.social_media.repository.PostRepo;
import com.bitsu.social_media.repository.ReactionRepo;
import com.bitsu.social_media.utility.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepo postRepo;
    private final UserService userService;
    private final CommentService commentService;
    private final ReactionRepo reactionRepo;
    private final S3Service s3Service;
    private final Utility utility;

    public List<PostResponse> getPosts(String search) {
        if (search == null || search.isBlank()) {
            return postRepo.findAllByOrderByCreatedAtDesc().stream()
                    .map(utility::mapToPostResponse)
                    .toList();
        }

        return postRepo.findPostsBySearchTerm(search).stream()
                .map(utility::mapToPostResponse)
                .toList();
    }

    public List<PostResponse> getFeed(String search) {
        if (search == null || search.isBlank()) {
            return postRepo.findPostsByFollowing(utility.getLoggedInUser().getFollowing().stream().map(user -> user.getId()).toList())
                    .stream()
                    .map(utility::mapToPostResponse)
                    .toList();
        }

        return postRepo.findPostsByFollowing(utility.getLoggedInUser().getFollowing().stream().map(user -> user.getId()).toList())
                .stream()
                .filter(post -> (post.getContent().toLowerCase().contains(search.toLowerCase()) || post.getUser().getUsername().toLowerCase().contains(search.toLowerCase())))
                .map(utility::mapToPostResponse)
                .toList();
    }



    public PostResponse createPost(PostRequest postRequest) {

        User user = utility.getLoggedInUser();

        Post post = Post.builder()
                .content(postRequest.getContent())
                .mediaURL(postRequest.getMediaURL())
                .user(user)
                .build();
        return utility.mapToPostResponse(postRepo.save(post));
    }

    public void deletePost(int id) {
        log.info("Delete: " + id);
        Post post = postRepo.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));
        if (post.getMediaURL() != null && !post.getMediaURL().isBlank()) {
            s3Service.deleteImageFromBucket(post.getMediaURL());
        }
        postRepo.delete(post);
    }

    public PostResponse updatePost(int id, PostRequest postRequest) {
        Post post = postRepo.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));
        post.setContent(postRequest.getContent());
        post.setMediaURL(postRequest.getMediaURL());
        return utility.mapToPostResponse(postRepo.save(post));
    }

    public void deleteReactions(int postId) {
        User user = utility.getLoggedInUser();
        Reaction reaction = reactionRepo.findByPostIdAndUserId(postId, user.getId())
                .orElseThrow(() -> new NotFoundException("Reaction not found"));
        reactionRepo.delete(reaction);
    }
}
