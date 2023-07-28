package com.LoFor1t.currencyExchangeSpring.RestControllers;

import com.LoFor1t.currencyExchangeSpring.DataModels.Currency;
import com.LoFor1t.currencyExchangeSpring.DataRepositories.CurrencyCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Object> createNewCurrency(@RequestParam("name") String name, @RequestParam("code") String code, @RequestParam("sign") String sign) {
        if (name == null || code == null) {
            return new ResponseEntity<>("Отсутствует нужное поле формы", HttpStatus.BAD_REQUEST);
        }

        if (name.isEmpty()) {
            return new ResponseEntity<>("Отсутствует имя валюты.", HttpStatus.BAD_REQUEST);
        }

        if (code.length() != 3) {
            return new ResponseEntity<>("Введён непраильный код валюты", HttpStatus.BAD_REQUEST);
        }

        if (currencyCrudRepository.existsByCode(code)) {
            return new ResponseEntity<>("Валюта с таким кодом уже существует", HttpStatus.CONFLICT);
        }

        Currency currency = new Currency(code, name, sign);

        currencyCrudRepository.save(currency);

        return new ResponseEntity<>(currency, HttpStatus.OK);
    }
}
