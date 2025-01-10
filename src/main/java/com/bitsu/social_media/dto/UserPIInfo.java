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
public class UserPIInfo {
    @NotBlank
    @Size(min = 3, max = 20, message = "First name must be between 3 and 20 characters")
    private  String firstname;
    @NotBlank
    @Size(min = 3, max = 20, message = "Last name must be between 3 and 20 characters")
    private  String lastname;
}
