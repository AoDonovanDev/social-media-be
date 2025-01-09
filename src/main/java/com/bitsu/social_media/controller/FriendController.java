package com.bitsu.social_media.controller;


import com.bitsu.social_media.dto.FriendDTO;
import com.bitsu.social_media.dto.FriendRequestDTO;
import com.bitsu.social_media.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
@Slf4j
public class FriendController {

    private final FriendService friendService;

    // Send a friend request
    @PostMapping("/{id}")
    public ResponseEntity<Void> addFriend(@PathVariable int id) {
        log.info("Sending friend request to user with ID: {}", id);
        friendService.addFriend(id);
        return ResponseEntity.ok().build();
    }

    // Accept a friend request
    @PutMapping("/requests/{requestId}/accept")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable int requestId) {
        log.info("Accepting friend request with ID: {}", requestId);
        friendService.acceptFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    // Decline a friend request
    @PutMapping("/requests/{requestId}/decline")
    public ResponseEntity<Void> declineFriendRequest(@PathVariable int requestId) {
        log.info("Declining friend request with ID: {}", requestId);
        friendService.declineFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    // Unfriend a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unfriend(@PathVariable int id) {
        log.info("Unfriending user with ID: {}", id);
        friendService.unfriend(id);
        return ResponseEntity.ok().build();
    }

    // Get a list of friends
    @GetMapping
    public ResponseEntity<List<FriendDTO>> getFriends() {
        log.info("Fetching friends list for the logged-in user");
        List<FriendDTO> friends = friendService.getFriends();
        return ResponseEntity.ok(friends);
    }

    // Check if two users are friends
    @GetMapping("/is-friend/{userId}")
    public ResponseEntity<Boolean> isFriend(@PathVariable int userId) {
        log.info("Checking friendship status with user ID: {}", userId);
        boolean isFriend = friendService.isFriend(userId);
        return ResponseEntity.ok(isFriend);
    }

    // Get pending friend requests
    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequestDTO>> getFriendRequests() {
        log.info("Fetching pending friend requests for the logged-in user");
        List<FriendRequestDTO> friendRequests = friendService.getFriendRequests();
        return ResponseEntity.ok(friendRequests);
    }
}
