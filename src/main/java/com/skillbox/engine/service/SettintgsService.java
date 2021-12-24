package com.skillbox.engine.service;

import com.skillbox.engine.api.response.SettingsResponse;
import com.skillbox.engine.model.entity.GlobalSetting;
import com.skillbox.engine.repository.GlobalSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettintgsService {
    private final GlobalSettingsRepository globalSettingsRepository;

    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        List<GlobalSetting> globalSettings = globalSettingsRepository.findAll();
        for (GlobalSetting setting : globalSettings) {
            switch (setting.getCode()) {
                case "MULTIUSER_MODE":
                    settingsResponse.setMultiuserMode(isYes(setting.getValue()));
                    break;
                case "POST_PREMODERATION":
                    settingsResponse.setPostPremoderation(isYes(setting.getValue()));
                    break;
                case "STATISTICS_IS_PUBLIC":
                    settingsResponse.setStatisticsPublic(isYes(setting.getValue()));
                    break;
            }
        }
        return settingsResponse;
    }

    private boolean isYes(String setting) {
        return setting.equals("YES");
    }
}
