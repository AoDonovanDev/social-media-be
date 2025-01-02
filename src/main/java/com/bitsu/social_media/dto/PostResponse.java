package com.bitsu.social_media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private int Id;
    private String content;
    private String mediaURL;
    private LocalDateTime createdAt;
    private UserResponse user;
    private List<CommentResponse> comments;
    private List<Integer> reactions;
}
