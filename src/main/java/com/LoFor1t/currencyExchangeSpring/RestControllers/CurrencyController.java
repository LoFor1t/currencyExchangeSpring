package com.LoFor1t.currencyExchangeSpring.RestControllers;

import com.LoFor1t.currencyExchangeSpring.DataModels.Currency;
import com.LoFor1t.currencyExchangeSpring.DataRepositories.CurrencyCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("currency")
public class CurrencyController {
    private final CurrencyCrudRepository currencyCrudRepository;

    public CurrencyController(CurrencyCrudRepository currencyCrudRepository) {
        this.currencyCrudRepository = currencyCrudRepository;
    }

    @GetMapping("/{code}")
    public ResponseEntity<Object> getCurrencyByCode(@PathVariable String code) {
        if (code.length() != 3) {
            return new ResponseEntity<>("Код валюты отсутствует в адресе.", HttpStatus.BAD_REQUEST);
        }

        Optional<Currency> currency = currencyCrudRepository.findByCode(code);
        if (currency.isEmpty()) {
            return new ResponseEntity<>("Валюта не найдена", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }
}
