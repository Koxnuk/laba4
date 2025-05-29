package com.example.currency.client;

import com.example.currency.exception.ApiException;
import com.example.currency.models.CurrencyInfo;
import com.example.currency.models.CurrencyRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class NbrbApiClient {
    private static final Logger logger = LoggerFactory.getLogger(NbrbApiClient.class);
    private static final String API_BASE_URL = "https://api.nbrb.by/exrates/";
    private final RestTemplate restTemplate;

    public NbrbApiClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<CurrencyInfo> getAllCurrencies() {
        String url = API_BASE_URL + "currencies";
        try {
            logger.info("Fetching all currencies from API: {}", url);
            ResponseEntity<CurrencyInfo[]> response = restTemplate.getForEntity(url, CurrencyInfo[].class);
            List<CurrencyInfo> currencies = Arrays.asList(Objects.requireNonNull(response.getBody()));
            logger.info("Successfully fetched {} currencies", currencies.size());
            return currencies;
        } catch (HttpClientErrorException e) {
            logger.error("Client error fetching currencies from API: {}", e.getMessage());
            throw new ApiException("Failed to fetch currencies: " + e.getMessage(), e, HttpStatus.BAD_REQUEST);
        } catch (HttpServerErrorException e) {
            logger.error("Server error fetching currencies from API: {}", e.getMessage());
            throw new ApiException("API server error: " + e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RestClientException e) {
            logger.error("Unexpected error fetching currencies from API: {}", e.getMessage());
            throw new ApiException("Unexpected error fetching currencies: " + e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public CurrencyRate getCurrencyRate(Integer curId) {
        String url = API_BASE_URL + "rates/" + curId;
        try {
            logger.info("Fetching currency rate for ID: {} from API: {}", curId, url);
            CurrencyRate rate = restTemplate.getForObject(url, CurrencyRate.class);
            logger.info("Successfully fetched rate for currency ID: {}", curId);
            return rate;
        } catch (HttpClientErrorException e) {
            logger.error("Client error fetching rate for currency ID {}: {}", curId, e.getMessage());
            throw new ApiException("Failed to fetch rate for currency ID " + curId + ": " + e.getMessage(), e, HttpStatus.BAD_REQUEST);
        } catch (HttpServerErrorException e) {
            logger.error("Server error fetching rate for currency ID {}: {}", curId, e.getMessage());
            throw new ApiException("API server error for currency ID " + curId + ": " + e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RestClientException e) {
            logger.error("Unexpected error fetching rate for currency ID {}: {}", curId, e.getMessage());
            throw new ApiException("Unexpected error fetching rate for currency ID " + curId + ": " + e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}