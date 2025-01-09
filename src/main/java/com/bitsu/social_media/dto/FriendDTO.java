package com.bitsu.social_media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendDTO {

    private int Id;
    private String username;
    private String bio;
    private String profilePicture;
    private  String firstname;
    private  String lastname;
}
