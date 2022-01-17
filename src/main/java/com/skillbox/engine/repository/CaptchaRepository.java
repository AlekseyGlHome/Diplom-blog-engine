package com.skillbox.engine.repository;

import com.skillbox.engine.model.entity.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

public interface CaptchaRepository extends JpaRepository<CaptchaCode, Integer> {

    @Transactional
    @Query("delete from CaptchaCode c where c.time <= :time")
    @Modifying
    int deleteByTimeBefore(LocalDateTime time);

}
