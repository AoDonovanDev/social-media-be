package com.bitsu.social_media.service;

import com.bitsu.social_media.dto.CommentRequest;
import com.bitsu.social_media.dto.CommentResponse;
import com.bitsu.social_media.exception.NotFoundException;
import com.bitsu.social_media.model.Comment;
import com.bitsu.social_media.model.Post;
import com.bitsu.social_media.model.User;
import com.bitsu.social_media.repository.CommentRepo;
import com.bitsu.social_media.repository.PostRepo;
import com.bitsu.social_media.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserService userService;
    private final CommentRepo commentRepo;
    private final PostRepo postRepo;
    private final Utility utility;
    public List<CommentResponse> getComments() {
        return commentRepo.findAllByOrderByCreatedAtDesc().stream()
                .map(utility::mapToCommentResponse)
                .toList();
    }



//    public PostCommentResponse mapToPostCommentResponse(Comment comment) {
//        return PostCommentResponse.builder()
//                .Id(comment.getId())
//                .content(comment.getContent())
//                .createdAt(comment.getCreatedAt())
//                .build();
//    }
    public CommentResponse createComment(CommentRequest commentRequest) {
        User user = utility.getLoggedInUser();

        Post post = postRepo.findById(commentRequest.getPostId())
                .orElseThrow(() -> new NotFoundException("Post not found"));

        var comment = Comment.builder()
                .content(commentRequest.getContent())
                .user(user)
                .post(post)
                .build();
        comment = commentRepo.save(comment);
        return utility.mapToCommentResponse(comment);
    }

    public void deleteComment(int id) {
        var comment = commentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        commentRepo.delete(comment);
    }

    public CommentResponse updateComment(int id, CommentRequest commentRequest) {
        var comment = commentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        comment.setContent(commentRequest.getContent());
        comment = commentRepo.save(comment);
        return utility.mapToCommentResponse(comment);
    }
}
