package com.bitsu.social_media.repository;

import com.bitsu.social_media.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Integer> {

    public List<Comment> findAllByOrderByCreatedAtDesc();

}
