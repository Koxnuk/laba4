package com.example.currency.controller;

import com.example.currency.models.CurrencyInfo;
import com.example.currency.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/currency/info")
public class CurrencyInfoController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyInfoController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<List<CurrencyInfo>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @GetMapping("/db")
    public ResponseEntity<List<CurrencyInfo>> getAllCurrenciesFromDb() {
        return ResponseEntity.ok(currencyService.getAllCurrenciesFromDb());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyInfo> getCurrencyById(@PathVariable Integer id) {
        Optional<CurrencyInfo> currency = currencyService.getCurrencyById(id);
        return currency.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CurrencyInfo> createCurrency(@Valid @RequestBody CurrencyInfo currencyInfo) {
        CurrencyInfo created = currencyService.createCurrency(currencyInfo);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyInfo> updateCurrency(@PathVariable Integer id, @Valid @RequestBody CurrencyInfo currencyInfo) {
        try {
            CurrencyInfo updated = currencyService.updateCurrency(id, currencyInfo);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Integer id) {
        try {
            currencyService.deleteCurrency(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}