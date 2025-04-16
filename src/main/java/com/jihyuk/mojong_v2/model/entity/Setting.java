package com.jihyuk.mojong_v2.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Setting {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String settingKey; // 예: qr_order, notice_on, maintenance_mode 등

    private String value; // 예: "true", "false", "message text" 등

    public void setValue(String value) {
        this.value = value;
    }

    public Setting(String settingKey) {
        this.settingKey = settingKey;
    }
}
