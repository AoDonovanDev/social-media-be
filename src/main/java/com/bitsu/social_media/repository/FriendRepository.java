package com.bitsu.social_media.repository;

import com.bitsu.social_media.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
    List<Friend> findByUserId(int userId);
    void deleteByUserIdAndFriendId(int userId, int friendId);

    boolean existsByUserIdAndFriendId(int userId, int friendId);
}