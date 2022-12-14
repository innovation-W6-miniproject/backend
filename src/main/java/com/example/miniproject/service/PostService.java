package com.example.miniproject.service;


import com.example.miniproject.domain.PostLikes;
import com.example.miniproject.dto.request.PostRequestDto;
import com.example.miniproject.dto.response.ImageResponseDto;
import com.example.miniproject.dto.response.PostResponseDto;
import com.example.miniproject.dto.response.ResponseDto;
import com.example.miniproject.domain.Member;
import com.example.miniproject.domain.Post;
import com.example.miniproject.jwt.TokenProvider;
import com.example.miniproject.repository.ImageRepository;
import com.example.miniproject.repository.PostLikesRepository;
import com.example.miniproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FileUploadService fileUploadService;
    private final ImageRepository imageRepository;
    private final PostLikesRepository postLikesRepository;
    private final TokenProvider tokenProvider;

    // 게시글 작성
    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto, MultipartFile multipartFile, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        //AWS
        String FileName = null;
        if (multipartFile.isEmpty()) {
            return ResponseDto.fail("INVALID_FILE", "파일이 유효하지 않습니다.");
        }
        ImageResponseDto imageResponseDto = null;
        try {
            FileName = fileUploadService.uploadFile(multipartFile, "image");
            imageResponseDto = new ImageResponseDto(FileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert imageResponseDto != null;

        //crawling
        Document doc = null;
        try {
            doc = Jsoup.connect(requestDto.getProductUrl()).get();    //Document에는 페이지의 전체 소스가 저장된다
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert doc != null;
        String img = Objects.requireNonNull(doc.select("meta[property^=og:image]").first()).attr("content");
        String title = doc.select("tbody tr td strong[class=color_2a line_h140]").text();
        String title2 = doc.select("div dl dt div[class=lowview_title_text]").text();
        String crwResult = (title.isBlank()) ? title2 : title;


        Post post = Post.builder()
                .member(member)
                .productUrl(requestDto.getProductUrl())
                .productName(crwResult)
                .productImg(img)
                .star(requestDto.getStar())
                .content(requestDto.getContent())
                .likes(0L)
                .imageUrl(FileName)
                .build();
        postRepository.save(post);

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .nickname(post.getMember().getNickname())
                        .productUrl(post.getProductUrl())
                        .productName(post.getProductName())
                        .productImg(post.getProductImg())
                        .star(post.getStar())
                        .content(post.getContent())
                        .imageUrl(post.getImageUrl())
                        .likes(post.getLikes())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }
    
    // 게시글 상세조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getPost(Long id, HttpServletRequest request) {
        Post post = isPresentPost(id);
        if(null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        boolean checkLike = checkLikeMember(post.getId(), member.getId());


        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .nickname(post.getMember().getNickname())
                        .productUrl(post.getProductUrl())
                        .productName(post.getProductName())
                        .productImg(post.getProductImg())
                        .star(post.getStar())
                        .content(post.getContent())
                        .imageUrl(post.getImageUrl())
                        .likes(post.getLikes())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .checkLike(checkLike)
                        .build()
        );
    }
    
    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPosts() {

        List<Post> allPosts = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (Post post : allPosts) {
            postResponseDtoList.add(
                PostResponseDto.builder()
                        .id(post.getId())
                        .nickname(post.getMember().getNickname())
                        .productUrl(post.getProductUrl())
                        .productName(post.getProductName())
                        .productImg(post.getProductImg())
                        .star(post.getStar())
                        .content(post.getContent())
                        .imageUrl(post.getImageUrl())
                        .likes(post.getLikes())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
            );
        }
        return ResponseDto.success(postResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> updatePost(Long id, PostRequestDto requestDto, MultipartFile multipartFile, HttpServletRequest request) {
        if(null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        if(null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if(null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(id);
        if(null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if(post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        //AWS
        String FileName = null;
        if (multipartFile.isEmpty()) {
            return ResponseDto.fail("INVALID_FILE", "파일이 유효하지 않습니다.");
        }
        ImageResponseDto imageResponseDto = null;
        if (!multipartFile.isEmpty()) {
            try {
                FileName = fileUploadService.uploadFile(multipartFile, "image");
                imageResponseDto = new ImageResponseDto(FileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        assert imageResponseDto != null;

        //crawling
        Document doc = null;
        try {
            doc = Jsoup.connect(requestDto.getProductUrl()).get();    //Document에는 페이지의 전체 소스가 저장된다
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert doc != null;
        String img = Objects.requireNonNull(doc.select("meta[property^=og:image]").first()).attr("content");
        String title = doc.select("tbody tr td strong[class=color_2a line_h140]").text();
        String title2 = doc.select("div dl dt div[class=lowview_title_text]").text();
        String crwResult = (title.isBlank()) ? title2 : title;

        post.update(requestDto, imageResponseDto, img, crwResult);

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .nickname(post.getMember().getNickname())
                        .productUrl(post.getProductUrl())
                        .productName(post.getProductName())
                        .productImg(post.getProductImg())
                        .star(post.getStar())
                        .content(post.getContent())
                        .imageUrl(post.getImageUrl())
                        .likes(post.getLikes())
                        .build()
        );
    }

    public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {
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

        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
        return ResponseDto.success("delete success");
    }

    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if(!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    public boolean checkLikeMember (Long postId, Long memberId){
        PostLikes postLikes = postLikesRepository.findByPostIdAndMemberId(postId,memberId);
        if(postLikes == null){
            return false;
        }
        return true;
    }


}