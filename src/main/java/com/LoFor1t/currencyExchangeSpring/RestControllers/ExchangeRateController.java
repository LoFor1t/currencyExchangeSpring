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
@RequestMapping("/exchangeRate/{codes}")
public class ExchangeRateController {
    private final ExchangeRateCrudRepository exchangeRateCrudRepository;
    private final CurrencyCrudRepository currencyCrudRepository;

    public ExchangeRateController(ExchangeRateCrudRepository exchangeRateCrudRepository, CurrencyCrudRepository currencyCrudRepository) {
        this.exchangeRateCrudRepository = exchangeRateCrudRepository;
        this.currencyCrudRepository = currencyCrudRepository;
    }

    @GetMapping
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

    @PatchMapping
    public ResponseEntity<Object> updateExchangeRateByCodes(@PathVariable String codes, @RequestParam("rate") String rate) {
        if (codes.length() != 6) {
            return new ResponseEntity<>("Код валютной пары отсутствует в адресе", HttpStatus.BAD_REQUEST);
        }

        if (rate.isEmpty()) {
            return new ResponseEntity<>("Rate отсутствует в параметрах", HttpStatus.BAD_REQUEST);
        }

        Optional<Currency> baseCurrency = currencyCrudRepository.findByCode(codes.substring(0, 3));
        Optional<Currency> targetCurrency = currencyCrudRepository.findByCode(codes.substring(3, 6));

        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            return new ResponseEntity<>("Одной из валют нет в базе данных.", HttpStatus.NOT_FOUND);
        }

        Optional<ExchangeRate> exchangeRate = exchangeRateCrudRepository.findByBaseCurrencyAndTargetCurrency(baseCurrency.get(), targetCurrency.get());

        if (exchangeRate.isEmpty()) {
            return new ResponseEntity<>("Данная валютная пара не существует в базе данных", HttpStatus.NOT_FOUND);
        }

        exchangeRate.get().setRate(new BigDecimal(rate));

        return new ResponseEntity<>(exchangeRateCrudRepository.save(exchangeRate.get()), HttpStatus.OK);
    }
}
