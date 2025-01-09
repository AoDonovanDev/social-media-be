package com.bitsu.social_media.dto;

import java.util.List;

import com.bitsu.social_media.model.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private int id;
    private String firstname;
    private String lastname;
    private String username;
    private String bio;
    private String profilePicture;
    private List<PostResponse> posts;
}
