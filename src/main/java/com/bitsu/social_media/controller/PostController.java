package com.bitsu.social_media.controller;

import com.bitsu.social_media.dto.PostRequest;
import com.bitsu.social_media.dto.PostResponse;
import com.bitsu.social_media.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPosts(
            @RequestParam String search
    ) {
        log.info("Get: ");
        return ResponseEntity.ok(postService.getPosts(search));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> getFeed(
            @RequestParam String search
    ) {
        log.info("Get: ");
        return ResponseEntity.ok(postService.getFeed(search));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PostResponse> createPost(
            @RequestBody PostRequest postRequest
    ) {
        log.info("Create: " + postRequest);
        return ResponseEntity.ok(postService.createPost(postRequest));
    }


    @DeleteMapping("/{id}")
    public void deletePost(
            @PathVariable int id
    ) {
        log.info("Delete: " + id);
        postService.deletePost(id);
        ResponseEntity.ok();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable int id,
            @RequestBody PostRequest postRequest
    ) {
        log.info("Update: " + id + " " + postRequest);
        return ResponseEntity.ok(postService.updatePost(id, postRequest));
    }

    @DeleteMapping("/reactions/{postId}")
    public void deleteReactions(
            @PathVariable int postId
    ) {
        log.info("Delete: " + postId);
        postService.deleteReactions(postId);
        ResponseEntity.ok();
    }


}
