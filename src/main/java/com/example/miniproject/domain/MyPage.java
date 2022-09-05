package com.example.miniproject.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MyPage extends Timestamped {
    @Id
    @Column(name = "mypage_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JoinColumn(name = "member_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;


    @JoinColumn(name = "post_id", nullable = false)
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Post> postList;

    public void update(Member member, List<Post> postResponseDtoList) {
        this.member = member;
        this.postList = postResponseDtoList;
    }

}
