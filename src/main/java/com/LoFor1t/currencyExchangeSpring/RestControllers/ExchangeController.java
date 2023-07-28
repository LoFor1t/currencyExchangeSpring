package com.LoFor1t.currencyExchangeSpring.RestControllers;

import com.LoFor1t.currencyExchangeSpring.DataModels.Currency;
import com.LoFor1t.currencyExchangeSpring.DataModels.Exchange;
import com.LoFor1t.currencyExchangeSpring.DataModels.ExchangeRate;
import com.LoFor1t.currencyExchangeSpring.DataRepositories.CurrencyCrudRepository;
import com.LoFor1t.currencyExchangeSpring.DataRepositories.ExchangeRateCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {
    private final CurrencyCrudRepository currencyCrudRepository;
    private final ExchangeRateCrudRepository exchangeRateCrudRepository;

    public ExchangeController(CurrencyCrudRepository currencyCrudRepository, ExchangeRateCrudRepository exchangeRateCrudRepository) {
        this.currencyCrudRepository = currencyCrudRepository;
        this.exchangeRateCrudRepository = exchangeRateCrudRepository;
    }

    @GetMapping
    public ResponseEntity<Object> getExchange(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("amount") String amount) {
        Optional<ExchangeRate> exchangeRate;
        Exchange exchange;

        if (from == null || to == null || amount == null) {
            return new ResponseEntity<>("Отсутствует нужное поле формы", HttpStatus.BAD_REQUEST);
        }

        if (from.length() != 3 || to.length() != 3) {
            return new ResponseEntity<>("Указан неверный код валютной пары", HttpStatus.BAD_REQUEST);
        }

        if (amount.isEmpty()) {
            return new ResponseEntity<>("Не указано поле amount", HttpStatus.BAD_REQUEST);
        }

        Optional<Currency> fromCurrency = currencyCrudRepository.findByCode(from);
        Optional<Currency> toCurrency = currencyCrudRepository.findByCode(to);

        if (fromCurrency.isEmpty() || toCurrency.isEmpty()) {
            return new ResponseEntity<>("Валюта отсутствует в базе данных", HttpStatus.NOT_FOUND);
        }

        exchangeRate = exchangeRateCrudRepository.findByBaseCurrencyAndTargetCurrency(fromCurrency.get(), toCurrency.get());

        if (!exchangeRate.isEmpty()) {
            BigDecimal convertedAmount = exchangeRate.get().getRate().multiply(new BigDecimal(amount));
            exchange = new Exchange(fromCurrency.get(), toCurrency.get(), exchangeRate.get().getRate(), new BigDecimal(amount), convertedAmount);
            return new ResponseEntity<>(exchange, HttpStatus.OK);
        }

        exchangeRate = exchangeRateCrudRepository.findByBaseCurrencyAndTargetCurrency(toCurrency.get(), fromCurrency.get());
        if (!exchangeRate.isEmpty()) {
            BigDecimal rate = BigDecimal.valueOf(1/exchangeRate.get().getRate().doubleValue());
            BigDecimal convertedAmount = rate.multiply(new BigDecimal(amount));
            exchange = new Exchange(fromCurrency.get(), toCurrency.get(), rate, new BigDecimal(amount), convertedAmount);
            return new ResponseEntity<>(exchange, HttpStatus.OK);
        }

        Optional<Currency> usdCurrency = currencyCrudRepository.findByCode("USD");

        if (usdCurrency.isPresent()) {
            Optional<ExchangeRate> exchange1 = exchangeRateCrudRepository.findByBaseCurrencyAndTargetCurrency(usdCurrency.get(), fromCurrency.get());
            Optional<ExchangeRate> exchange2 = exchangeRateCrudRepository.findByBaseCurrencyAndTargetCurrency(usdCurrency.get(), toCurrency.get());
            if (exchange1.isPresent() && exchange2.isPresent()) {
                BigDecimal rate = BigDecimal.valueOf(exchange2.get().getRate().doubleValue() / exchange1.get().getRate().doubleValue());
                BigDecimal convertedAmount = rate.multiply(new BigDecimal(amount));
                exchange = new Exchange(fromCurrency.get(), toCurrency.get(), rate, new BigDecimal(amount), convertedAmount);
                return new ResponseEntity<>(exchange, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("We can't convert this", HttpStatus.NOT_FOUND);
    }
}
