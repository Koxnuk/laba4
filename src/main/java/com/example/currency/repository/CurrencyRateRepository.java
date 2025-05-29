package com.example.currency.repository;

import com.example.currency.models.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    List<CurrencyRate> findByAbbreviationAndDate(String abbreviation, LocalDate date);
}