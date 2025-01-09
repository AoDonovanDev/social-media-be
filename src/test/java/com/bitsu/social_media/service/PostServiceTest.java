package com.bitsu.social_media.service;

import com.bitsu.social_media.dto.PostRequest;
import com.bitsu.social_media.dto.PostResponse;
import com.bitsu.social_media.model.Post;
import com.bitsu.social_media.model.User;
import com.bitsu.social_media.repository.PostRepo;
import com.bitsu.social_media.repository.ReactionRepo;
import com.bitsu.social_media.utility.Utility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepo postRepo;

    @Mock
    private UserService userService;

    @Mock
    private Utility utility;

    @Mock
    private CommentService commentService;

    @Mock
    private ReactionRepo reactionRepo;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPost_ShouldReturnPostResponse() {
        PostRequest request = new PostRequest();
        request.setContent("Test content");
        request.setMediaURL("http://media.url");

        User user = new User();
        user.setId(1);

        Post post = new Post();
        post.setId(1);
        post.setContent("Test content");
        post.setMediaURL("http://media.url");
        post.setUser(user);

        when(utility.getLoggedInUser()).thenReturn(user);
        when(postRepo.save(any(Post.class))).thenReturn(post);

        PostResponse response = postService.createPost(request);

        assertNotNull(response);
        assertEquals("Test content", response.getContent());
    }

    @Test
    void getPosts_ShouldReturnListOfPostResponses() {
        Post post = new Post();
        post.setId(1);
        post.setContent("Test content");

        when(postRepo.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(post));

        List<PostResponse> responses = postService.getPosts(null);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Test content", responses.get(0).getContent());
    }

    @Test
    void updatePost_ShouldReturnUpdatedPostResponse() {
        PostRequest request = new PostRequest();
        request.setContent("Updated content");
        request.setMediaURL("http://updated.url");

        Post post = new Post();
        post.setId(1);
        post.setContent("Test content");

        when(postRepo.findById(anyInt())).thenReturn(Optional.of(post));
        when(postRepo.save(any(Post.class))).thenReturn(post);

        PostResponse response = postService.updatePost(1, request);

        assertNotNull(response);
        assertEquals("Updated content", response.getContent());
    }

    @Test
    void deletePost_ShouldDeletePost() {
        Post post = new Post();
        post.setId(1);

        when(postRepo.findById(anyInt())).thenReturn(Optional.of(post));
        doNothing().when(postRepo).delete(any(Post.class));

        postService.deletePost(1);

        verify(postRepo, times(1)).delete(post);
    }
}