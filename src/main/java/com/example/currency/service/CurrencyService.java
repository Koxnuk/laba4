package com.example.currency.service;

import com.example.currency.exception.ApiException;
import com.example.currency.models.CurrencyInfo;
import com.example.currency.repository.CurrencyInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyService.class);
    private final CurrencyInfoRepository currencyInfoRepository;

    @Autowired
    public CurrencyService(CurrencyInfoRepository currencyInfoRepository) {
        this.currencyInfoRepository = currencyInfoRepository;
    }

    public List<CurrencyInfo> getAllCurrencies() {
        return currencyInfoRepository.findAll();
    }

    public List<CurrencyInfo> getAllCurrenciesFromDb() {
        return currencyInfoRepository.findAll();
    }

    public Optional<CurrencyInfo> getCurrencyById(Integer id) {
        return currencyInfoRepository.findById(id);
    }

    public CurrencyInfo createCurrency(CurrencyInfo currencyInfo) {
        if (currencyInfo.getAbbreviation() == null || currencyInfo.getAbbreviation().trim().isEmpty()) {
            logger.error("Аббревиатура валюты не может быть пустой");
            throw new ApiException("Аббревиатура валюты не может быть пустой", HttpStatus.BAD_REQUEST);
        }
        return currencyInfoRepository.save(currencyInfo);
    }

    public CurrencyInfo updateCurrency(Integer id, CurrencyInfo currencyInfo) {
        if (currencyInfo.getAbbreviation() == null || currencyInfo.getAbbreviation().trim().isEmpty()) {
            logger.error("Аббревиатура валюты не может быть пустой");
            throw new ApiException("Аббревиатура валюты не может быть пустой", HttpStatus.BAD_REQUEST);
        }
        currencyInfo.setId(id);
        return currencyInfoRepository.save(currencyInfo);
    }

    public void deleteCurrency(Integer id) {
        currencyInfoRepository.deleteById(id);
    }
}