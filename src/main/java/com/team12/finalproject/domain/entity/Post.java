package com.team12.finalproject.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team12.finalproject.domain.dto.post.PostListResponse;
import lombok.*;
import org.springframework.data.domain.Page;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    private String title;
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;
    private LocalDateTime deletedAt;



    public static Post save(User user, String title, String body) {
        return Post.builder()
                .user(user)
                .title(title)
                .body(body)
                .build();
    }

}
