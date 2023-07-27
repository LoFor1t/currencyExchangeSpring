package com.LoFor1t.currencyExchangeSpring.RestControllers;

import com.LoFor1t.currencyExchangeSpring.DataModels.Currency;
import com.LoFor1t.currencyExchangeSpring.DataRepositories.CurrencyCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currencies")
public class CurrenciesController {
    private final CurrencyCrudRepository currencyCrudRepository;

    public CurrenciesController(CurrencyCrudRepository currencyCrudRepository) {
        this.currencyCrudRepository = currencyCrudRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<Currency>> getAllCurrencies() {
        return new ResponseEntity<>(currencyCrudRepository.findAll(), HttpStatus.OK);
    }
}
