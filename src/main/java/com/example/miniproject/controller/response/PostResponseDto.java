package com.example.miniproject.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostResponseDto {

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
