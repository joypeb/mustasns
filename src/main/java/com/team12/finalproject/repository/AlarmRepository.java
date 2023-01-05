package com.team12.finalproject.repository;

import com.team12.finalproject.domain.entity.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Integer> {

    Optional<Alarm> findByTargetIdAndFromUserId(int targetId, int fromUserId);

    Page<Alarm> findAllByUser_UserName(String userName, Pageable pageable);

    Page<Alarm> findAllByTargetId(int targetId, Pageable pageable);
}
