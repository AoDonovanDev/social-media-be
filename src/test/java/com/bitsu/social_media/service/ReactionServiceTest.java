package com.bitsu.social_media.service;

import com.bitsu.social_media.dto.ReactionRequest;
import com.bitsu.social_media.dto.ReactionResponse;
import com.bitsu.social_media.model.Post;
import com.bitsu.social_media.model.Reaction;
import com.bitsu.social_media.model.ReactionType;
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

class ReactionServiceTest {

    @Mock
    private ReactionRepo reactionRepo;

    @Mock
    private UserService userService;

    @Mock
    private Utility utility;

    @Mock
    private PostRepo postRepo;

    @InjectMocks
    private ReactionService reactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReaction_ShouldReturnReactionResponse() {
        ReactionRequest request = new ReactionRequest();
        request.setType(ReactionType.LIKE);
        request.setPostId(1);

        User user = new User();
        user.setId(1);

        Post post = new Post();
        post.setId(1);

        Reaction reaction = new Reaction();
        reaction.setId(1);
        reaction.setType(ReactionType.LIKE);
        reaction.setUser(user);
        reaction.setPost(post);

        when(utility.getLoggedInUser()).thenReturn(user);
        when(postRepo.findById(anyInt())).thenReturn(Optional.of(post));
        when(reactionRepo.save(any(Reaction.class))).thenReturn(reaction);

        ReactionResponse response = reactionService.createReaction(request);

        assertNotNull(response);
        assertEquals(ReactionType.LIKE, response.getType());
    }

    @Test
    void getReactions_ShouldReturnListOfReactionResponses() {
        Reaction reaction = new Reaction();
        reaction.setId(1);
        reaction.setType(ReactionType.LIKE);

        when(reactionRepo.findAll()).thenReturn(List.of(reaction));

        List<ReactionResponse> responses = reactionService.getReactions();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(ReactionType.LIKE, responses.get(0).getType());
    }

    @Test
    void updateReaction_ShouldReturnUpdatedReactionResponse() {
        ReactionRequest request = new ReactionRequest();
        request.setType(ReactionType.LIKE);

        Reaction reaction = new Reaction();
        reaction.setId(1);
        reaction.setType(ReactionType.LIKE);

        when(reactionRepo.findById(anyInt())).thenReturn(Optional.of(reaction));
        when(reactionRepo.save(any(Reaction.class))).thenReturn(reaction);

        ReactionResponse response = reactionService.updateReaction(1, request);

        assertNotNull(response);
        assertEquals(ReactionType.LIKE, response.getType());
    }

    @Test
    void deleteReaction_ShouldDeleteReaction() {
        Reaction reaction = new Reaction();
        reaction.setId(1);

        when(reactionRepo.findById(anyInt())).thenReturn(Optional.of(reaction));
        doNothing().when(reactionRepo).delete(any(Reaction.class));

        reactionService.deleteReaction(1);

        verify(reactionRepo, times(1)).delete(reaction);
    }
}