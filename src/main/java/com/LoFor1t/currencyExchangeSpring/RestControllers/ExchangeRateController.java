package com.LoFor1t.currencyExchangeSpring.RestControllers;

import com.LoFor1t.currencyExchangeSpring.DataModels.Currency;
import com.LoFor1t.currencyExchangeSpring.DataModels.ExchangeRate;
import com.LoFor1t.currencyExchangeSpring.DataRepositories.CurrencyCrudRepository;
import com.LoFor1t.currencyExchangeSpring.DataRepositories.ExchangeRateCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/exchangeRate")
public class ExchangeRateController {
    private final ExchangeRateCrudRepository exchangeRateCrudRepository;
    private final CurrencyCrudRepository currencyCrudRepository;

    public ExchangeRateController(ExchangeRateCrudRepository exchangeRateCrudRepository, CurrencyCrudRepository currencyCrudRepository) {
        this.exchangeRateCrudRepository = exchangeRateCrudRepository;
        this.currencyCrudRepository = currencyCrudRepository;
    }

    @GetMapping("/{codes}")
    public ResponseEntity<Object> getExchangeRateByCurrencyCodes(@PathVariable String codes) {
        if (codes.length() != 6) {
            return new ResponseEntity<>("Код валютной пары отсутствует в адресе", HttpStatus.BAD_REQUEST);
        }

        Optional<Currency> baseCurrency = currencyCrudRepository.findByCode(codes.substring(0, 3));
        Optional<Currency> targetCurrency = currencyCrudRepository.findByCode(codes.substring(3, 6));

        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            return new ResponseEntity<>("Одной из валют нет в базе данных.", HttpStatus.NOT_FOUND);
        }

        Optional<ExchangeRate> exchangeRate = exchangeRateCrudRepository.findByBaseCurrencyAndTargetCurrency(baseCurrency.get(), targetCurrency.get());

        if (exchangeRate.isEmpty()) {
            return new ResponseEntity<>("Обменный курс для пары не найден.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(exchangeRate, HttpStatus.OK);
    }
}
