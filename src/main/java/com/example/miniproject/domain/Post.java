package com.example.miniproject.domain;

import com.example.miniproject.dto.request.PostRequestDto;
import com.example.miniproject.dto.response.ImageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productUrl;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer star;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = true)
    private Long likes;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    // cascade = CascadeType.ALL 상위 엔티티에서 하위 엔티티로 모든 작업을 전파
    // orphanRemoval = true 부모가 삭제되면 자식도 함께 삭제, 관계가 끊어진 자식을 자동 제거
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLikes> postLikes;

    public void update(PostRequestDto postRequestDto, ImageResponseDto imageResponseDto) {
        this.productUrl = postRequestDto.getProductUrl();
        this.productName = postRequestDto.getProductName();
        this.star = postRequestDto.getStar();
        this.content = postRequestDto.getContent();
        this.imageUrl = imageResponseDto.getImageUrl();
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

}
