package com.LoFor1t.currencyExchangeSpring.RestControllers;

import com.LoFor1t.currencyExchangeSpring.DataRepositories.ExchangeRateCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchangeRates")
public class ExchangeRatesController {
    private final ExchangeRateCrudRepository exchangeRateCrudRepository;

    public ExchangeRatesController(ExchangeRateCrudRepository exchangeRateCrudRepository) {
        this.exchangeRateCrudRepository = exchangeRateCrudRepository;
    }

    @GetMapping
    public ResponseEntity<Object> getAllExchangeRates() {
        return new ResponseEntity<>(exchangeRateCrudRepository.findAll(), HttpStatus.OK);
    }
}
