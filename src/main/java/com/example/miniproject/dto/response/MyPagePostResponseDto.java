package com.example.miniproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPagePostResponseDto {
    private Long id;
    private String nickname;
    private String productUrl;
    private String productName;
    private Integer star;
    private String content;
    private String imageUrl;
    private Long likes;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
