package com.bitsu.social_media.repository;

import com.bitsu.social_media.model.Post;
import com.bitsu.social_media.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer>{

    public List<Post> findAllByOrderByCreatedAtDesc();

    public List<Post> findAllByContentContains(String search);

    @Query("SELECT p FROM Post p WHERE p.user.id IN :following")
    List<Post> findPostsByFollowing(@Param("following") List<Integer> following);

    public List<Post> findAllByUser(User user);

}
