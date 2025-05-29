package com.example.currency.models;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
@Data
public class CurrencyRate {
    @Id
    private Long id;
    private BigDecimal rate;
    private String abbreviation;
    private java.time.LocalDate date;
}