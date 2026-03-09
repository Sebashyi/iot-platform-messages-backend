package com.m3verificaciones.appweb.messages.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.m3verificaciones.appweb.messages.util.unique_id.UniqueIdGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "messages_decoded_younio")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDecodedYounio {
    @Id
    @Column(length = 10, unique = true, nullable = false, name = "unique_key")
    private String uniqueKey;

    @Column(name = "dev_eui")
    private String devEui;

    @Column(name = "serial")
    private String serial;

    @Column(name = "alarmas")
    private String alarmas;

    @Column(name = "reading")
    private Double reading;

    @Column(name = "voltage")
    private Double voltage;

    @Column(name = "unit")
    private Double unit;

    @Column(name = "raw_data")
    private String rawData;

    @Column(name = "low_battery_alarm")
    private Boolean lowBatteryAlarm;

    @Column(name = "empty_pipe_alarm")
    private Boolean emptyPipeAlarm;

    @Column(name = "reverse_flow_alarm")
    private Boolean reverseFlowAlarm;

    @Column(name = "over_range_alarm")
    private Boolean overRangeAlarm;

    @Column(name = "over_temprature_alarm")
    private Boolean overTemperatureAlarm;

    @Column(name = "eeprom_error")
    private Boolean eepromError;

    @Column(name = "leakage_alarm")
    private Boolean leakageAlarm;

    @Column(name = "burst_alarm")
    private Boolean burstAlarm;

    @Column(name = "tamper_alarm")
    private Boolean tamperAlarm;

    @Column(name = "freezing_alarm")
    private Boolean freezingAlarm;

    @Column(name = "data_kind")
    private String dataKind;

    @Column(name = "back_reading")
    private Long backReading;

    @Column(name = "mnf")
    private Long mnf;

    @Column(name = "sensor_signal")
    private String sensorSignal;

    @Column(name = "sensor_state")
    private String sensorState;

    @Column(name = "type_message")
    private String typeMessage;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Transient
    @JsonIgnore
    private UniqueIdGenerator key;

    @PrePersist
    protected void onCreate() {
        this.uniqueKey = UniqueIdGenerator.generateUniqueKey();
    }
}
