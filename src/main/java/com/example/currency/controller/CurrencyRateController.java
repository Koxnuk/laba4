package com.example.currency.controller;

import com.example.currency.exception.ApiException;
import com.example.currency.models.CurrencyRate;
import com.example.currency.service.CurrencyConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/currency/rates")
@Tag(name = "Курсы валют", description = "API для управления курсами валют и конвертацией")
public class CurrencyRateController {
    private final CurrencyConversionService conversionService;

    public CurrencyRateController(CurrencyConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Operation(summary = "Конвертировать валюту", description = "Конвертирует сумму из одной валюты в другую.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Сумма успешно конвертирована"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные параметры"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/convert")
    public ResponseEntity<?> convert(
            @RequestParam Integer from,
            @RequestParam Integer to,
            @RequestParam BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException("Сумма должна быть больше нуля", HttpStatus.BAD_REQUEST);
        }
        BigDecimal result = conversionService.convertCurrency(from, to, amount);
        return ResponseEntity.ok(Map.of(
                "amount", amount,
                "from", from,
                "to", to,
                "result", result
        ));
    }

    @Operation(summary = "Получить все курсы", description = "Извлекает все доступные курсы валют.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курсы успешно получены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<CurrencyRate>> getAllRates() {
        return ResponseEntity.ok(conversionService.getAllRates());
    }

    @Operation(summary = "Получить курс по ID", description = "Извлекает конкретный курс валюты по его ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс успешно получен"),
            @ApiResponse(responseCode = "404", description = "Курс не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CurrencyRate> getRateById(@PathVariable Long id) {
        Optional<CurrencyRate> rate = conversionService.getRateById(id);
        return rate.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать новый курс", description = "Создаёт новую запись о курсе валюты.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные курса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<CurrencyRate> createRate(@Valid @RequestBody CurrencyRate rate) {
        CurrencyRate created = conversionService.createRate(rate);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Обновить курс", description = "Обновляет существующий курс валюты по его ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные курса"),
            @ApiResponse(responseCode = "404", description = "Курс не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CurrencyRate> updateRate(@PathVariable Long id, @Valid @RequestBody CurrencyRate rate) {
        CurrencyRate updated = conversionService.updateRate(id, rate);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Удалить курс", description = "Удаляет курс валюты по его ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Курс успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Курс не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRate(@PathVariable Long id) {
        conversionService.deleteRate(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить курсы по аббревиатуре и дате", description = "Извлекает курсы валют для указанной аббревиатуры и даты.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курсы успешно получены"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные параметры"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/by-abbreviation")
    public ResponseEntity<List<CurrencyRate>> getRatesByAbbreviationAndDate(
            @RequestParam String abbreviation,
            @RequestParam LocalDate date) {
        List<CurrencyRate> rates = conversionService.getRatesByAbbreviationAndDate(abbreviation, date);
        return ResponseEntity.ok(rates);
    }
}