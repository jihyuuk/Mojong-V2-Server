package com.jihyuk.mojong_v2.service;

import com.jihyuk.mojong_v2.model.entity.Setting;
import com.jihyuk.mojong_v2.repository.SettingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;

    @Transactional
    public boolean isQrOrderEnabled() {
        return settingRepository.findBySettingKey("qr_order")
                .map(setting -> Boolean.parseBoolean(setting.getValue()))
                .orElse(false);
    }

    @Transactional
    public void setQrOrderEnabled(boolean enabled) {
        Setting setting = settingRepository.findBySettingKey("qr_order")
                .orElseGet(() -> new Setting("qr_order"));

        setting.setValue(String.valueOf(enabled));
        settingRepository.save(setting);
    }


}
