package com.team12.finalproject.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
@SQLDelete(sql = "UPDATE likes SET deleted_at = current_timestamp WHERE id = ?")
public class Like extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    @JsonIgnore
    private Post post;

    private LocalDateTime deletedAt;

    public static Like save(User user, Post post, LocalDateTime deletedAt) {
        return Like.builder()
                .user(user)
                .post(post)
                .deletedAt(deletedAt)
                .build();
    }
}
