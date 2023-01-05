package com.team12.finalproject.domain.dto.alarm;

import com.team12.finalproject.domain.entity.Alarm;
import com.team12.finalproject.domain.role.AlarmType;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmResponse {
    private int id;
    private AlarmType alarmType;
    private int fromUserId;
    private int targetId;
    private String text;
    private LocalDateTime createdAt;

    public static Page<AlarmResponse> alarmList(Page<Alarm> alarms) {
        return alarms.map(alarm -> AlarmResponse.builder()
                .id(alarm.getId())
                .alarmType(alarm.getAlarmType())
                .fromUserId(alarm.getFromUserId())
                .targetId(alarm.getTargetId())
                .text(alarm.getText())
                .createdAt(alarm.getCreatedAt())
                .build());
    }
}
