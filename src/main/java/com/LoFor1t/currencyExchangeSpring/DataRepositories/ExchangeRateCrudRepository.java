package com.LoFor1t.currencyExchangeSpring.DataRepositories;

import com.LoFor1t.currencyExchangeSpring.DataModels.ExchangeRate;
import org.springframework.data.repository.CrudRepository;

public interface ExchangeRateCrudRepository extends CrudRepository<ExchangeRate, Integer> {
}
