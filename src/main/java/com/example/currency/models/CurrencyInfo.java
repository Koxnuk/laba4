package com.example.currency.models;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Data
public class CurrencyInfo {
    @Id
    private Integer id;
    private String abbreviation;
}