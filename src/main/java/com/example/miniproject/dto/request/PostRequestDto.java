package com.example.miniproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    private String productUrl;
    private String productName;
    private Integer star;
    private String content;
    private String imageUrl;
}
