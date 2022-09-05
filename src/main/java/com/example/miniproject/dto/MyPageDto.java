package com.example.miniproject.dto;

import com.example.miniproject.dto.response.MyPagePostResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MyPageDto {

    private List<MyPagePostResponseDto> postList;

    public void update(List<MyPagePostResponseDto> postResponseDtoList) {
        this.postList = postResponseDtoList;
    }

}
