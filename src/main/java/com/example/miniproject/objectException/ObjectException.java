package com.example.miniproject.objectException;

import com.example.miniproject.domain.Post;

public class ObjectException {

    public static void postValidate(Post post) {
        if(post.getId() == null || post.getId() <= 0) {
            throw new IllegalArgumentException("유효하지 않는 Post Id 입니다.");
        }
    }
}
