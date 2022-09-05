package com.example.miniproject.dto;

import com.example.miniproject.dto.response.MyPagePostLikesResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyLikePageDto {

    private List<MyPagePostLikesResponseDto> PostLikesList;

    public void update(List<MyPagePostLikesResponseDto> myPagePostLikesResponseDtoList) {
        this.PostLikesList = myPagePostLikesResponseDtoList;
    }
}
