package com.example.miniproject.controller;

import com.example.miniproject.controller.request.PostLikesRequestDto;
import com.example.miniproject.controller.response.ResponseDto;
import com.example.miniproject.domain.PostLikes;
import com.example.miniproject.service.PostLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor
@RestController
public class PostLikesController {

    private final PostLikesService postLikesService;

    @PostMapping(value = "api/auth/like/post/{postId}")
    public ResponseDto<?> createPostLikes(@RequestBody PostLikesRequestDto requestsDto, HttpServletRequest request) {
        return postLikesService.createPostLikes(requestsDto, request);
    }

}
