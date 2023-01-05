package com.team12.finalproject.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team12.finalproject.domain.role.AlarmType;
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
@SQLDelete(sql = "UPDATE alarm SET deleted_at = current_timestamp WHERE id = ?")
@Where(clause = "deleted_at is NULL")
public class Alarm extends BaseEntity{
    //fromUserId : 알림을 발생시킨 아이디
    //targetId : 알림이 발생된 post의 id

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private AlarmType alarmType;
    private int fromUserId;
    private int targetId;
    private String text;
    private LocalDateTime deleted_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    public static Alarm save(AlarmType alarmType, Post post, User user) {
        String text = "";
        if(alarmType.equals(AlarmType.NEW_COMMENT_ON_POST)) text = "new comment!";
        else if(alarmType.equals(AlarmType.NEW_LIKE_ON_POST)) text = "new like!";

        return Alarm.builder()
                .alarmType(alarmType)
                .fromUserId(user.getId())
                .targetId(post.getId())
                .text(text)
                .deleted_at(null)
                .user(post.getUser())
                .build();
    }
}
