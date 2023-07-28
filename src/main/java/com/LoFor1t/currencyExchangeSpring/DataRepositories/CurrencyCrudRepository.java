package com.LoFor1t.currencyExchangeSpring.DataRepositories;

import com.LoFor1t.currencyExchangeSpring.DataModels.Currency;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CurrencyCrudRepository extends CrudRepository<Currency, Integer> {
    Optional<Currency> findByCode(String code);
    Boolean existsByCode(String code);
}
