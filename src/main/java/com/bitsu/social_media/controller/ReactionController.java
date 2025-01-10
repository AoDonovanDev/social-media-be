package com.bitsu.social_media.controller;

import com.bitsu.social_media.dto.ReactionRequest;
import com.bitsu.social_media.dto.ReactionResponse;
import com.bitsu.social_media.service.ReactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/reactions")
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionService reactionService;

    @GetMapping
    public ResponseEntity<List<ReactionResponse>> getReactions() {
        return ResponseEntity.ok(reactionService.getReactions());
    }

    @PostMapping
    public ResponseEntity<ReactionResponse> createReaction(
            @Valid @RequestBody ReactionRequest reactionRequest
    ) {
        return ResponseEntity.ok(reactionService.createReaction(reactionRequest));
    }

    @DeleteMapping("/{id}")
    public void deleteReaction(
            @PathVariable int id
    ) {
        reactionService.deleteReaction(id);
        ResponseEntity.ok();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReactionResponse> updateReaction(
            @PathVariable int id,
            @RequestBody ReactionRequest reactionRequest
    ) {
        return ResponseEntity.ok(reactionService.updateReaction(id, reactionRequest));
    }


}
