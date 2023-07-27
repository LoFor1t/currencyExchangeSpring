package com.LoFor1t.currencyExchangeSpring.DataModels;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "exchangeRates", uniqueConstraints = {@UniqueConstraint(columnNames = {"baseCurrencyId", "targetCurrencyId"})})
@NoArgsConstructor
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "baseCurrencyId", nullable = false)
    private Currency baseCurrency;

    @ManyToOne
    @JoinColumn(name = "targetCurrencyId", nullable = false)
    private Currency targetCurrency;

    @Column(name = "rate")
    private BigDecimal rate;

    public ExchangeRate(Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }
}
