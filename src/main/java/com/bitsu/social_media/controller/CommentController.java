package com.bitsu.social_media.controller;

import com.bitsu.social_media.dto.CommentRequest;
import com.bitsu.social_media.dto.CommentResponse;
import com.bitsu.social_media.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments() {
        log.error("Get: ");
        return ResponseEntity.ok(commentService.getComments());
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @Valid @RequestBody CommentRequest commentRequest
    ) {
        log.error("Create: " + commentRequest);
        return ResponseEntity.ok(commentService.createComment(commentRequest));
    }

    @DeleteMapping("/{id}")
    public void deleteComment(
            @PathVariable int id
    ) {
        log.error("Delete: " + id);
        commentService.deleteComment(id);
        ResponseEntity.ok();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable int id,
            @Valid @RequestBody CommentRequest commentRequest
    ) {
        log.error("Update: " + id + " " + commentRequest);
        return ResponseEntity.ok(commentService.updateComment(id, commentRequest));
    }
}
