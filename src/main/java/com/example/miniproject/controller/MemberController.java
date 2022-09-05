package com.example.miniproject.controller;

import com.example.miniproject.dto.request.LoginRequestDto;
import com.example.miniproject.dto.request.MemberRequestDto;
import com.example.miniproject.dto.ResponseDto;
import com.example.miniproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("api/member/signup")
    public ResponseDto<?> signup (@RequestBody @Valid MemberRequestDto requestDto) {
        return memberService.signup(requestDto);
    }

    @PostMapping("api/member/login")
    public ResponseDto<?> login (@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response) {
        return memberService.login(requestDto, response);
    }

    @PostMapping( "/api/auth/member/logout")
    public ResponseDto<?> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }

}
