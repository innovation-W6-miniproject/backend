package com.example.miniproject.controller;

import com.example.miniproject.dto.response.ResponseDto;
import com.example.miniproject.service.PostLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor
@RestController
public class PostLikesController {

    private final PostLikesService postLikesService;

    @PostMapping(value = "api/auth/like/post/{postId}")
    public ResponseDto<?> togglePostLikes(@PathVariable Long postId, HttpServletRequest request) {
        return postLikesService.togglePostLikes(postId, request);
    }

}
