package com.m3verificaciones.appweb.messages.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "meter")
public class Meter {
    @Id
    @Column(name = "unique_key")
    private String uniqueKey;

    @Column(name = "dev_eui")
    private String devEui;

    @Column(name = "company_unique_key")
    private String companyUniqueKey;
}
