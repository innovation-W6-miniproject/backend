package com.example.miniproject.controller;


import com.example.miniproject.controller.request.PostRequestDto;
import com.example.miniproject.controller.response.ResponseDto;
import com.example.miniproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
// final 필드나, @NonNull 이 붙은 필드에 생성자를 생성해준다, 의존성주입 편의성을 위해 사용
@RequiredArgsConstructor    
public class PostController {

    private final PostService postService;
    
    // 게시글 작성
    @PostMapping(value = "/api/auth/post")
    public ResponseDto<?> createPost(@RequestPart PostRequestDto requestDto, @RequestPart MultipartFile multipartFile, HttpServletRequest request) {
        return postService.createPost(requestDto, multipartFile, request);
    }

    // 게시글 상세조회
    @GetMapping(value = "/api/post/{id}")
    public ResponseDto<?> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // 게시글 전체조회
    @GetMapping(value = "/api/auth/post/{postId}")
    public ResponseDto<?> getAllPosts(){
        return postService.getAllPosts();
    }

    // 게시글 수정
    @PutMapping(value = "/api/auth/post/{postid}")
    public ResponseDto<?> updatePost(@PathVariable Long id, @RequestPart PostRequestDto requestDto, @RequestPart MultipartFile multipartFile, HttpServletRequest request) {
        return postService.updatePost(id, requestDto, multipartFile, request);
    }

    // 게시글 삭제
    @DeleteMapping(value = "/api/auth/post/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id, request);
    }
}
