package com.example.miniproject.service;

import com.example.miniproject.controller.request.PostLikesRequestDto;
import com.example.miniproject.controller.response.ResponseDto;
import com.example.miniproject.domain.Member;
import com.example.miniproject.domain.Post;
import com.example.miniproject.domain.PostLikes;
import com.example.miniproject.jwt.TokenProvider;
import com.example.miniproject.repository.PostLikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class PostLikesService {

    private final PostService postService;
    private final PostLikesRepository postLikesRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> createPostLikes(PostLikesRequestDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = postService.isPresentPost(requestDto.getId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        PostLikes findPostLikes = postLikesRepository.findByPostIdAndMemberId(post.getId(), member.getId());
        if ( null != findPostLikes ) {
            postLikesRepository.delete(findPostLikes);
            return  ResponseDto.success("좋아요 취소");
        }

        PostLikes postLikes = PostLikes.builder()
                .member(member)
                .post(post)
                .build();
        postLikesRepository.save(postLikes);
            return  ResponseDto.success("좋아요 완료");
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}
