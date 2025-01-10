package com.bitsu.social_media.service;


import com.bitsu.social_media.dto.FriendDTO;
import com.bitsu.social_media.dto.FriendRequestDTO;
import com.bitsu.social_media.exception.AlreadyFriendsException;
import com.bitsu.social_media.exception.FriendRequestExistsException;
import com.bitsu.social_media.exception.NotFoundException;
import com.bitsu.social_media.exception.SelfFriendRequestException;
import com.bitsu.social_media.model.Friend;
import com.bitsu.social_media.model.FriendRequest;
import com.bitsu.social_media.model.FriendRequestStatus;
import com.bitsu.social_media.model.User;
import com.bitsu.social_media.repository.FriendRepository;
import com.bitsu.social_media.repository.FriendRequestRepository;
import com.bitsu.social_media.repository.UserRepo;
import com.bitsu.social_media.utility.Utility;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final UserRepo userRepo;
    private final Utility utility;

    // Send a friend request from the logged-in user to the receiver
    public void addFriend(int id) {
        User user = utility.getLoggedInUser();
        User userToFriend = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User to friend not found"));

        // Prevent sending a friend request to yourself
        if (user.getId() == userToFriend.getId()) {
            throw new SelfFriendRequestException("You cannot send a friend request to yourself.");
        }

        // Check if a friend request already exists in either direction
        boolean requestExists = friendRequestRepository.existsBySenderIdAndReceiverId(user.getId(), userToFriend.getId())
                || friendRequestRepository.existsBySenderIdAndReceiverId(userToFriend.getId(), user.getId());

        if (requestExists) {
            throw new FriendRequestExistsException("A friend request already exists between these users.");
        }

        // Check if they are already friends
        if (friendRepository.existsByUserIdAndFriendId(user.getId(), userToFriend.getId())) {
            throw new AlreadyFriendsException("You are already friends with this user.");
        }

        // Create a new friend request
        FriendRequest friendRequest = FriendRequest.builder()
                .sender(user)
                .receiver(userToFriend)
                .status(FriendRequestStatus.PENDING)
                .build();

        friendRequestRepository.save(friendRequest);
    }

    // Accept a friend request by the receiver
    public void acceptFriendRequest(int requestId) {
        User user = utility.getLoggedInUser();
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Friend request not found."));

        if (request.getReceiver().getId() != (user.getId())) {
            throw new RuntimeException("Unauthorized.");
        }

        request.setStatus(FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(request);
        log.info(request.toString());
        friendRepository.save(Friend.builder()
                        .user(request.getReceiver())
                        .friend(request.getSender())
                .build());
        friendRepository.save(Friend.builder()
                .friend(request.getReceiver())
                .user(request.getSender())
                .build());
    }

    // Decline a friend request by the receiver
    public void declineFriendRequest(int requestId) {
        User user = utility.getLoggedInUser();
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Friend request not found."));

        if (request.getReceiver().getId() != (user.getId())) {
            throw new RuntimeException("Unauthorized.");
        }

        request.setStatus(FriendRequestStatus.DECLINED);
        friendRequestRepository.save(request);
    }

    // Unfriend a user
    @Transactional
    public void unfriend(int id) {
        User user = utility.getLoggedInUser();
        User friend = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User to unfriend not found"));

        friendRepository.deleteByUserIdAndFriendId(user.getId(), friend.getId());
        friendRepository.deleteByUserIdAndFriendId(friend.getId(), user.getId());
    }

    // Get a list of friends for the logged-in user
    public List<FriendDTO> getFriends() {
        User user = utility.getLoggedInUser();
        return friendRepository.findByUserId(user.getId()).stream()
                .map(friend -> FriendDTO.builder()
                        .Id(friend.getId())
                        .profilePicture(friend.getFriend().getProfilePicture())
                        .username(friend.getFriend().getUsername())
                        .firstname(friend.getFriend().getFirstname())
                        .lastname(friend.getFriend().getLastname())
                        .bio(friend.getFriend().getBio())
                        .build())
                .toList();
    }

    // Check if two users are friends
    public boolean isFriend(int userId) {
        User user = utility.getLoggedInUser();
        return friendRepository.existsByUserIdAndFriendId(user.getId(), userId);
    }

    // Get pending friend requests for the logged-in user
    public List<FriendRequestDTO> getFriendRequests() {
        User user = utility.getLoggedInUser();
        return friendRequestRepository.findByReceiverId(user.getId()).stream()
                .filter(request -> request.getStatus() == FriendRequestStatus.PENDING)
                .map(request -> {
                    User senderProfile = request.getSender();

                    return FriendRequestDTO.builder()
                            .requestId(request.getId())
                            .Id(senderProfile.getId())
                            .profilePicture(senderProfile.getProfilePicture())
                            .username(senderProfile.getUsername())
                            .build();
                })
                .toList();
    }
}