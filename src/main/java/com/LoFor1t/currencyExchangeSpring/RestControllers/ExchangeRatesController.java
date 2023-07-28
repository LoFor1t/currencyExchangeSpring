package com.LoFor1t.currencyExchangeSpring.RestControllers;

import com.LoFor1t.currencyExchangeSpring.DataModels.Currency;
import com.LoFor1t.currencyExchangeSpring.DataModels.ExchangeRate;
import com.LoFor1t.currencyExchangeSpring.DataRepositories.CurrencyCrudRepository;
import com.LoFor1t.currencyExchangeSpring.DataRepositories.ExchangeRateCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/exchangeRates")
public class ExchangeRatesController {
    private final ExchangeRateCrudRepository exchangeRateCrudRepository;
    private final CurrencyCrudRepository currencyCrudRepository;

    public ExchangeRatesController(ExchangeRateCrudRepository exchangeRateCrudRepository, CurrencyCrudRepository currencyCrudRepository) {
        this.exchangeRateCrudRepository = exchangeRateCrudRepository;
        this.currencyCrudRepository = currencyCrudRepository;
    }

    @GetMapping
    public ResponseEntity<Object> getAllExchangeRates() {
        return new ResponseEntity<>(exchangeRateCrudRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createNewExchangeRate(@RequestParam("baseCurrencyCode") String baseCurrencyCode, @RequestParam("targetCurrencyCode") String targetCurrencyCode, @RequestParam("rate") String rate) {
        if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null) {
            return new ResponseEntity<>("Отсутствует нужное поле формы", HttpStatus.BAD_REQUEST);
        }

        if (rate.isEmpty()) {
            return new ResponseEntity<>("Отсутствует поле rate", HttpStatus.BAD_REQUEST);
        }

        if (baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
            return new ResponseEntity<>("Введен неверный код для валютной пары", HttpStatus.BAD_REQUEST);
        }

        Optional<Currency> baseCurrency = currencyCrudRepository.findByCode(baseCurrencyCode);
        Optional<Currency> targetCurrency = currencyCrudRepository.findByCode(targetCurrencyCode);

        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            return new ResponseEntity<>("Валюта с таким кодом не существует", HttpStatus.BAD_REQUEST);
        }

        if (exchangeRateCrudRepository.existsByBaseCurrencyAndTargetCurrency(baseCurrency.get(), targetCurrency.get())) {
            return new ResponseEntity<>("Валютнай пара с таким кодом уже существует.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(exchangeRateCrudRepository.save(new ExchangeRate(baseCurrency.get(), targetCurrency.get(), new BigDecimal(rate))), HttpStatus.CREATED);
    }
}
