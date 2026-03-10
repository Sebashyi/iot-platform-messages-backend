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
@Table(name = "messages")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Messages {

    @Id
    @Column(length = 10, unique = true, nullable = false, name = "unique_key")
    private String uniqueKey;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "type_message")
    private String typeMessage;

    private String communication;

    @NotBlank
    @Column(name = "dev_eui")
    private String devEui;

    private double freq;

    private String dr;

    private int fcnt;

    private int port;

    private boolean confirmed;

    @Column(columnDefinition = "TEXT")
    private String gws;

    @Column(name = "raw_reading", columnDefinition = "TEXT")
    private String rawReading;

    @NotBlank
    @Column(name = "serial_meter")
    private String serialMeter;


    @Transient
    @JsonIgnore
    private UniqueIdGenerator key;

    @PrePersist
    protected void onCreate() {
        this.uniqueKey = UniqueIdGenerator.generateUniqueKey();
    }
}
