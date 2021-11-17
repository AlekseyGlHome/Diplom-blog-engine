package com.skillbox.engine.repository;

import com.skillbox.engine.model.entity.GlobalSetting;
import org.springframework.data.repository.CrudRepository;

public interface GlobalSettingsRepository extends CrudRepository<GlobalSetting, Integer> {
}
