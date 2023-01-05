package com.team12.finalproject.service;

import com.team12.finalproject.domain.dto.alarm.AlarmResponse;
import com.team12.finalproject.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    //특정 유저의 알림 받기
    public Page<AlarmResponse> getAlarms(String userName, Pageable pageable) {
        return AlarmResponse.alarmList(alarmRepository.findAllByUser_UserName(userName,pageable));
    }

    //특정 post의 알림 받기
    public Page<AlarmResponse> getPostAlarms(int postId, Pageable pageable) {
        return AlarmResponse.alarmList(alarmRepository.findAllByTargetId(postId,pageable));
    }
}
