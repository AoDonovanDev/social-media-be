package com.bitsu.social_media.service;


import com.bitsu.social_media.dto.ReactionRequest;
import com.bitsu.social_media.dto.ReactionResponse;
import com.bitsu.social_media.model.Reaction;
import com.bitsu.social_media.repository.PostRepo;
import com.bitsu.social_media.repository.ReactionRepo;
import com.bitsu.social_media.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReactionService {
    private final ReactionRepo reactionRepo;
    private final UserService userService;
    private final PostRepo postRepo;
    private final Utility utility;

    public List<ReactionResponse> getReactions() {
        return reactionRepo.findAll().stream()
                .map(this::mapToReactionResponse)
                .toList();
    }

    public ReactionResponse mapToReactionResponse(Reaction reaction) {
        return ReactionResponse.builder()
                .id(reaction.getId())
                .type(reaction.getType())
                .userResponse(
                        utility.mapToUserResponse(reaction.getUser())
                )
                .build();
    }

//    public PostReactionResponse mapToPostReactionResponse(Reaction reaction) {
//        return PostReactionResponse.builder()
//                .id(reaction.getId())
//                .type(reaction.getType())
//                .build();
//    }
//

    public ReactionResponse createReaction(ReactionRequest reactionRequest) {
        var post = postRepo.findById(reactionRequest.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Reaction reaction = Reaction.builder()
                .type(reactionRequest.getType())
                .user(utility.getLoggedInUser())
                .post(post)
                .build();

        return mapToReactionResponse(reactionRepo.save(reaction));

    }

    public void deleteReaction(int id) {
        var reaction = reactionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reaction not found"));

        reactionRepo.delete(reaction);
    }

    public ReactionResponse updateReaction(int id, ReactionRequest reactionRequest) {
        var reaction = reactionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reaction not found"));

            reaction.setType(reactionRequest.getType());

            return mapToReactionResponse(reactionRepo.save(reaction));
    }
}