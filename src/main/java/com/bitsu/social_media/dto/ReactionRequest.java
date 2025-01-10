package com.bitsu.social_media.dto;

import com.bitsu.social_media.model.ReactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReactionRequest {
    @NotNull
    @NotBlank
    private ReactionType type;

    @NotNull
    private int postId;
}
