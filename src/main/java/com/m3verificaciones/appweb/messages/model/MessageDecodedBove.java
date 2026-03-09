package com.m3verificaciones.appweb.messages.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.m3verificaciones.appweb.messages.util.unique_id.UniqueIdGenerator;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "messages_decoded_bove")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDecodedBove {

    @Id
    @Column(length = 10, unique = true, nullable = false, name = "unique_key")
    private String uniqueKey;

    @Column(name = "type_message")
    private String typeMessage;

    @Column(name = "control_code")
    private String controlCode;

    @Column(name = "date_length")
    private String dateLength;

    @Column(name = "data_identification1")
    private String datadeIntification1;

    @Column(name = "data_identification2")
    private String dataIdentification2;

    @Column(name = "count_number")
    private String countNumber;

    private Double unit;

    @Column(name = "volume_data")
    private Double volumeData;

    @Column(name = "meter_state_st1")
    private String meterStateSt1;

    @Column(name = "meter_state_st2")
    private String meterStateSt2;

    @Column(name = "battery_capacity")
    private String batteryCapacity;

    @Column(name = "low_battery_alarm")
    private Boolean lowBatteryAlarm;

    @Column(name = "empty_pipe_alarm")
    private Boolean emptyPipeAlarm;

    @Column(name = "reverse_flow_alarm")
    private Boolean reverseFlowAlarm;

    @Column(name = "over_range_alarm")
    private Boolean overRangeAlarm;

    @Column(name = "over_temprature_alarm")
    private Boolean overTempratureAlarm;

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

    @Column(name = "raw_data")
    private String rawData;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @NotBlank
    @Column(name = "dev_eui")
    private String devEui;

    private String error;

    @Column(name = "valve_state")
    private String valveState;

    @Transient
    @JsonIgnore
    private UniqueIdGenerator key;

    @PrePersist
    protected void onCreate() {
        this.uniqueKey = UniqueIdGenerator.generateUniqueKey();
    }
}
