package com.example.miniproject.service;

import com.example.miniproject.domain.Member;
import com.example.miniproject.domain.Post;
import com.example.miniproject.domain.PostLikes;
import com.example.miniproject.dto.*;
import com.example.miniproject.dto.response.MyPagePostLikesResponseDto;
import com.example.miniproject.dto.response.MyPagePostResponseDto;
import com.example.miniproject.dto.response.ResponseDto;
import com.example.miniproject.jwt.TokenProvider;
import com.example.miniproject.repository.PostLikesRepository;
import com.example.miniproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final TokenProvider tokenProvider;
    private final PostRepository postRepository;
    private final PostLikesRepository postLikesRepository;


    @Transactional
    public ResponseDto<?> getMyPost(HttpServletRequest request){
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        List<Post> postList = postRepository.findByMember(member);
        List<MyPagePostResponseDto> postResponseDtoList = new ArrayList<>();

        for (Post post : postList) {
            postResponseDtoList.add(
                    MyPagePostResponseDto.builder()
                            .id(post.getId())
                            .nickname(post.getMember().getNickname())
                            .productUrl(post.getProductUrl())
                            .productImg(post.getProductImg())
                            .productName(post.getProductName())
                            .star(post.getStar())
                            .content(post.getContent())
                            .imageUrl((post.getImageUrl()))
                            .likes(post.getLikes())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }


        MyPageDto myPageDto = new MyPageDto();
        myPageDto.update(postResponseDtoList);
        return ResponseDto.success(myPageDto);

    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }



    //좋아요 누른 것들 불러오기
    @Transactional
    public ResponseDto<MyLikePageDto> getMyPostLikes(HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        List<MyPagePostLikesResponseDto> myPagePostLikesResponseDtoList = new ArrayList<>();

        List<PostLikes> postLikesList = postLikesRepository.findByMember(member);
        for (PostLikes postLikes : postLikesList) {
            myPagePostLikesResponseDtoList.add(
                    MyPagePostLikesResponseDto.builder()
                            .id(postLikes.getPost().getId())
                            .nickname(postLikes.getPost().getMember().getNickname())
                            .productUrl(postLikes.getPost().getProductUrl())
                            .productImg(postLikes.getPost().getProductImg())
                            .productName(postLikes.getPost().getProductName())
                            .star(postLikes.getPost().getStar())
                            .content(postLikes.getPost().getContent())
                            .imageUrl((postLikes.getPost().getImageUrl()))
                            .likes(postLikes.getPost().getLikes())
                            .createdAt(postLikes.getPost().getCreatedAt())
                            .modifiedAt(postLikes.getPost().getModifiedAt())
                            .build()
            );
        }


        MyLikePageDto myLikePageDto = new MyLikePageDto();
        myLikePageDto.update(myPagePostLikesResponseDtoList);
        return ResponseDto.success(myLikePageDto);
    }
}


