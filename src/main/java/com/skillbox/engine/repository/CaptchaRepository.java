package com.skillbox.engine.repository;

import com.skillbox.engine.model.entity.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public interface CaptchaRepository extends JpaRepository<CaptchaCode, Integer> {

    @Transactional
    @Modifying
    @Query("delete from CaptchaCode c where c.time <= :time")
    int deleteByTimeBefore(LocalDateTime time);


    Optional<CaptchaCode> findBySecretCodeEquals(String secretCode);


}
