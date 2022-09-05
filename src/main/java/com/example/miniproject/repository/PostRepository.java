package com.example.miniproject.repository;

import com.example.miniproject.domain.Member;
import com.example.miniproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByModifiedAtDesc();
    List<Post> findByMember(Member member);
}
