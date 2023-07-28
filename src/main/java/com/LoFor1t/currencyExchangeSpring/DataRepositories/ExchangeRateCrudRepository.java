package com.LoFor1t.currencyExchangeSpring.DataRepositories;

import com.LoFor1t.currencyExchangeSpring.DataModels.Currency;
import com.LoFor1t.currencyExchangeSpring.DataModels.ExchangeRate;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ExchangeRateCrudRepository extends CrudRepository<ExchangeRate, Integer> {
    Optional<ExchangeRate> findByBaseCurrencyAndTargetCurrency(Currency baseCurrency, Currency targetCurrency);
    Boolean existsByBaseCurrencyAndTargetCurrency(Currency baseCurrency, Currency targetCurrency);
}
