package com.example.miniproject.domain;

import com.example.miniproject.objectException.ObjectException;
import com.example.miniproject.objectException.URLException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String imageUrl;

    public Image(Post post, String imageUrl) {
        URLException.URLException(imageUrl);
        ObjectException.postValidate(post);
        this.post = post;
        this.imageUrl = imageUrl;
    }
}
