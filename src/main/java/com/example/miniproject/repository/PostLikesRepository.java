package com.example.miniproject.repository;

import com.example.miniproject.domain.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Member;
import java.util.List;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {
    Long countAllByPostId(Long postId);
    PostLikes findByPostIdAndMemberId(Long postId, Long memberId);
    List<PostLikes> findAllByMember(Member member);
}
