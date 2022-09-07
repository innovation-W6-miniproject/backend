package com.example.miniproject.service;

import com.example.miniproject.domain.Member;
import com.example.miniproject.dto.*;
import com.example.miniproject.dto.request.LoginRequestDto;
import com.example.miniproject.dto.request.MemberRequestDto;
import com.example.miniproject.dto.response.MemberResponseDto;
import com.example.miniproject.dto.response.ResponseDto;
import com.example.miniproject.jwt.TokenProvider;
import com.example.miniproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;


    @Transactional
    public ResponseDto<?> signup(MemberRequestDto requestDto) {
        if (null != isPresentMember(requestDto.getUserId())) {
            return ResponseDto.fail("DUPLICATED_USERID","중복된 아이디 입니다.");
        }

        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            return ResponseDto.fail("PASSWORDS_NOT_MATCHED", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        Member member = Member.builder()
                .userId(requestDto.getUserId())
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        memberRepository.save(member);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .userId(member.getUserId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );

    }


    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {

        Member member = isPresentMember(requestDto.getUserId());
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail("INVALID_MEMBER", "사용자를 찾을 수 없습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);



        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .userId(member.getUserId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );

    }

    public ResponseDto<?> logout(HttpServletRequest request) {
        if(!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        return tokenProvider.deleteRefreshToken(member);
    }


    public boolean checkId(MemberRequestDto memberRequestDto) {
        String userId = memberRequestDto.getUserId();
        return !memberRepository.findByUserId(userId).isPresent();//userid와 같은게 있는지 리파지토리에서 찾아보고, isPresent() 이게 있냐 없냐를 확인, ! = 있으면 false
    }

    public boolean checkNickname(MemberRequestDto memberRequestDto) {
        String nickname = memberRequestDto.getNickname();
        return !memberRepository.findByNickname(nickname).isPresent();
    }

    @Transactional(readOnly = true)
    public Member isPresentMember(String userId) {
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        return optionalMember.orElse(null);
    }


    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

}
