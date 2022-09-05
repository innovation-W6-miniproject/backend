package com.example.miniproject.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MyLikePage {

    @Id
    @Column(name = "likeMypage_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JoinColumn(name = "member_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;


    @JoinColumn(name = "PostLikes_id", nullable = false)
    @ManyToMany(fetch = FetchType.LAZY)
    private List<PostLikes> postLikesList;

    public void update ( Member member, List<PostLikes> MyPagePostLikesResponseDtoList) {
        this.member = member;
        this.postLikesList = MyPagePostLikesResponseDtoList;
    }

}
