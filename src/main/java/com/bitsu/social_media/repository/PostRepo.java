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

    @Query("SELECT p FROM Post p WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.user.username) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Post> findPostsBySearchTerm(@Param("searchTerm") String searchTerm);

    @Query("SELECT p FROM Post p WHERE p.user.id IN :following")
    List<Post> findPostsByFollowing(@Param("following") List<Integer> following);

    public List<Post> findAllByUser(User user);

}
