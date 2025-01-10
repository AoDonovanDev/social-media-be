package com.bitsu.social_media.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfilePic {
    @Size(max = 255, message = "Profile picture must be less than 255 characters")
    private String profilePicture;
}
