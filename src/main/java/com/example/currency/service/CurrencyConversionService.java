package com.example.currency.service;

import com.example.currency.exception.ApiException;
import com.example.currency.models.CurrencyRate;
import com.example.currency.repository.CurrencyRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyConversionService {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionService.class);
    private final CurrencyRateRepository rateRepository;

    @Autowired
    public CurrencyConversionService(CurrencyRateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public BigDecimal convertCurrency(Integer fromId, Integer toId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Недопустимая сумма для конвертации: {}", amount);
            throw new ApiException("Сумма должна быть больше нуля", HttpStatus.BAD_REQUEST);
        }

        Optional<CurrencyRate> fromRateOpt = rateRepository.findById((long) fromId);
        Optional<CurrencyRate> toRateOpt = rateRepository.findById((long) toId);

        if (fromRateOpt.isEmpty() || toRateOpt.isEmpty()) {
            logger.error("Валюты с ID {} или {} не найдены", fromId, toId);
            throw new ApiException("Одна из валют не найдена", HttpStatus.NOT_FOUND);
        }

        CurrencyRate fromRate = fromRateOpt.get();
        CurrencyRate toRate = toRateOpt.get();

        if (fromRate.getRate() == null || toRate.getRate() == null) {
            logger.error("Курс для валюты с ID {} или {} равен null", fromId, toId);
            throw new ApiException("Курс валюты отсутствует", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (fromRate.getRate().compareTo(BigDecimal.ZERO) <= 0 || toRate.getRate().compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Недопустимые курсы для валют с ID {} или {}", fromId, toId);
            throw new ApiException("Недопустимые курсы валют", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        BigDecimal result = amount.multiply(toRate.getRate()).divide(fromRate.getRate(), 4, RoundingMode.HALF_UP);
        logger.info("Конвертация выполнена: {} {} -> {} {}", amount, fromId, result, toId);
        return result;
    }

    public List<CurrencyRate> getAllRates() {
        return rateRepository.findAll();
    }

    public Optional<CurrencyRate> getRateById(Long id) {
        return rateRepository.findById(id);
    }

    public CurrencyRate createRate(CurrencyRate rate) {
        if (rate.getRate() == null || rate.getRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException("Курс должен быть больше нуля", HttpStatus.BAD_REQUEST);
        }
        return rateRepository.save(rate);
    }

    public CurrencyRate updateRate(Long id, CurrencyRate rate) {
        if (rate.getRate() == null || rate.getRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException("Курс должен быть больше нуля", HttpStatus.BAD_REQUEST);
        }
        rate.setId(id);
        return rateRepository.save(rate);
    }

    public void deleteRate(Long id) {
        rateRepository.deleteById(id);
    }

    public List<CurrencyRate> getRatesByAbbreviationAndDate(String abbreviation, LocalDate date) {
        return rateRepository.findByAbbreviationAndDate(abbreviation, date);
    }
}