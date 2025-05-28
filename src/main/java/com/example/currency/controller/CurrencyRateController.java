package com.example.currency.controller;

import com.example.currency.models.CurrencyRate;
import com.example.currency.service.CurrencyConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/currency/rates")
public class CurrencyRateController {

    private final CurrencyConversionService conversionService;

    @Autowired
    public CurrencyRateController(CurrencyConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @GetMapping("/convert")
    public ResponseEntity<?> convert(
            @RequestParam Integer from,
            @RequestParam Integer to,
            @RequestParam BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Amount must be greater than zero")
            );
        }
        try {
            BigDecimal result = conversionService.convertCurrency(from, to, amount);
            return ResponseEntity.ok(Map.of(
                    "amount", amount,
                    "from", from,
                    "to", to,
                    "result", result
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Conversion error: " + e.getMessage())
            );
        }
    }

    @GetMapping
    public ResponseEntity<List<CurrencyRate>> getAllRates() {
        return ResponseEntity.ok(conversionService.getAllRates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyRate> getRateById(@PathVariable Long id) {
        Optional<CurrencyRate> rate = conversionService.getRateById(id);
        return rate.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CurrencyRate> createRate(@Valid @RequestBody CurrencyRate rate) {
        CurrencyRate created = conversionService.createRate(rate);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyRate> updateRate(@PathVariable Long id, @Valid @RequestBody CurrencyRate rate) {
        try {
            CurrencyRate updated = conversionService.updateRate(id, rate);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRate(@PathVariable Long id) {
        try {
            conversionService.deleteRate(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-abbreviation")
    public ResponseEntity<List<CurrencyRate>> getRatesByAbbreviationAndDate(
            @RequestParam String abbreviation,
            @RequestParam LocalDate date) {
        List<CurrencyRate> rates = conversionService.getRatesByAbbreviationAndDate(abbreviation, date);
        return ResponseEntity.ok(rates);
    }
}