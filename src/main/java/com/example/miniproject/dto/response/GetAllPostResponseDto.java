package com.example.miniproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllPostResponseDto {

    private Long id;
    private String productUrl;
    private String productName;
    private Integer star;
    private Long likes;
}
