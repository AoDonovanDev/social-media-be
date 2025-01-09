package com.bitsu.social_media.service;
import com.bitsu.social_media.dto.CommentRequest;
import com.bitsu.social_media.dto.CommentResponse;
import com.bitsu.social_media.model.Comment;
import com.bitsu.social_media.model.Post;
import com.bitsu.social_media.model.User;
import com.bitsu.social_media.repository.CommentRepo;
import com.bitsu.social_media.repository.PostRepo;
import com.bitsu.social_media.repository.UserRepo;
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
public class CommentServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private Utility utility;

    @Mock
    private CommentRepo commentRepo;

    @Mock
    private PostRepo postRepo;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createComment_ShouldReturnCommentResponse() {
        CommentRequest request = new CommentRequest();
        request.setContent("Test comment");
        request.setPostId(1);

        User user = new User();
        user.setId(1);

        Post post = new Post();
        post.setId(1);

        Comment comment = new Comment();
        comment.setId(1);
        comment.setContent("Test comment");
        comment.setUser(user);
        comment.setPost(post);

        when(utility.getLoggedInUser()).thenReturn(user);
        when(postRepo.findById(anyInt())).thenReturn(Optional.of(post));
        when(commentRepo.save(any(Comment.class))).thenReturn(comment);

        CommentResponse response = commentService.createComment(request);

        assertNotNull(response);
        assertEquals("Test comment", response.getContent());
    }

    @Test
    void getComments_ShouldReturnListOfCommentResponses() {
        Comment comment = new Comment();
        comment.setId(1);
        comment.setContent("Test comment");

        when(commentRepo.findAll()).thenReturn(List.of(comment));

        List<CommentResponse> responses = commentService.getComments();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Test comment", responses.get(0).getContent());
    }

    @Test
    void updateComment_ShouldReturnUpdatedCommentResponse() {
        CommentRequest request = new CommentRequest();
        request.setContent("Updated comment");

        Comment comment = new Comment();
        comment.setId(1);
        comment.setContent("Test comment");

        when(commentRepo.findById(anyInt())).thenReturn(Optional.of(comment));
        when(commentRepo.save(any(Comment.class))).thenReturn(comment);

        CommentResponse response = commentService.updateComment(1, request);

        assertNotNull(response);
        assertEquals("Updated comment", response.getContent());
    }

    @Test
    void deleteComment_ShouldDeleteComment() {
        Comment comment = new Comment();
        comment.setId(1);

        when(commentRepo.findById(anyInt())).thenReturn(Optional.of(comment));
        doNothing().when(commentRepo).delete(any(Comment.class));

        commentService.deleteComment(1);

        verify(commentRepo, times(1)).delete(comment);
    }
}