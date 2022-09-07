package com.example.miniproject.controller;


import com.example.miniproject.dto.response.ResponseDto;
import com.example.miniproject.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class MyPageContorller {

    private final MyPageService myPageService;

    @GetMapping("/api/auth/mypage")
    public ResponseDto<?> getMyPost(HttpServletRequest request) {
        return myPageService.getMyPost(request);
    }

    @GetMapping("/api/auth/mypage/like")
    public ResponseDto<?> getMyPostLikes(HttpServletRequest request) {
        return myPageService.getMyPostLikes(request);
    }


}
