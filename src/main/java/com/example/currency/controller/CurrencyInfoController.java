package com.example.currency.controller;

import com.example.currency.models.CurrencyInfo;
import com.example.currency.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/currency/info")
@Tag(name = "Информация о валютах", description = "API для управления информацией о валютах")
public class CurrencyInfoController {
    private final CurrencyService currencyService;

    public CurrencyInfoController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Operation(summary = "Получить все валюты", description = "Извлекает все доступные валюты из кэша или API.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Валюты успешно получены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<CurrencyInfo>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @Operation(summary = "Получить все валюты из базы данных", description = "Извлекает все валюты, хранящиеся в базе данных.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Валюты из базы данных успешно получены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/db")
    public ResponseEntity<List<CurrencyInfo>> getAllCurrenciesFromDb() {
        return ResponseEntity.ok(currencyService.getAllCurrenciesFromDb());
    }

    @Operation(summary = "Получить валюту по ID", description = "Извлекает конкретную валюту по её ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Валюта успешно получена"),
            @ApiResponse(responseCode = "404", description = "Валюта не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CurrencyInfo> getCurrencyById(@PathVariable Integer id) {
        Optional<CurrencyInfo> currency = currencyService.getCurrencyById(id);
        return currency.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать новую валюту", description = "Создаёт новую запись о валюте.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Валюта успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные валюты"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<CurrencyInfo> createCurrency(@Valid @RequestBody CurrencyInfo currencyInfo) {
        CurrencyInfo created = currencyService.createCurrency(currencyInfo);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Обновить валюту", description = "Обновляет существующую валюту по её ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Валюта успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные валюты"),
            @ApiResponse(responseCode = "404", description = "Валюта не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CurrencyInfo> updateCurrency(@PathVariable Integer id, @Valid @RequestBody CurrencyInfo currencyInfo) {
        CurrencyInfo updated = currencyService.updateCurrency(id, currencyInfo);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Удалить валюту", description = "Удаляет валюту по её ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Валюта успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Валюта не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Integer id) {
        currencyService.deleteCurrency(id);
        return ResponseEntity.noContent().build();
    }
}