package com.bitsu.social_media.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
        @NotBlank
        @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
        private String content;

        private String mediaURL;
}
