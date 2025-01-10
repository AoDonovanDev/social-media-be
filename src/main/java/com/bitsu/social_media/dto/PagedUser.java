package com.bitsu.social_media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagedUser {
    List<UserResponse> users;
    int totalPages;
    int currentPage;
    int size;
    boolean hasNext;
}
