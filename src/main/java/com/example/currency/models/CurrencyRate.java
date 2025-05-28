package com.example.currency.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "currency_rate")
@Data
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("Cur_OfficialRate")
    @jakarta.persistence.Column(name = "cur_official_rate")
    private BigDecimal curOfficialRate;

    @JsonProperty("Cur_Scale")
    @jakarta.persistence.Column(name = "cur_scale")
    private Integer curScale;

    @JsonProperty("Date")
    @jakarta.persistence.Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private CurrencyInfo currency;
}