package com.bitsu.social_media.repository;

import com.bitsu.social_media.model.FriendRequest;
import com.bitsu.social_media.model.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
    List<FriendRequest> findByReceiverIdAndStatus(int receiverId, FriendRequestStatus status);

    List<FriendRequest> findByReceiverId(int userId);

    // This method should check if a friend request exists and return a boolean
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FriendRequest f WHERE f.sender.id = :senderId AND f.receiver.id = :receiverId")
    boolean existsBySenderIdAndReceiverId(@Param("senderId") int senderId, @Param("receiverId") int receiverId);

    // This method should retrieve the FriendRequest object if it exists
    Optional<FriendRequest> findBySenderIdAndReceiverId(int senderId, int receiverId);
}