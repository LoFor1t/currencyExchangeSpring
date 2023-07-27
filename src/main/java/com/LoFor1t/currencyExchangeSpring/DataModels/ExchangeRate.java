package com.LoFor1t.currencyExchangeSpring.DataModels;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "exchangerates", uniqueConstraints = {@UniqueConstraint(columnNames = {"basecurrencyid", "targetcurrencyid"})})
@NoArgsConstructor
@Getter
@Setter
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "basecurrencyid", nullable = false)
    private Currency baseCurrency;

    @ManyToOne
    @JoinColumn(name = "targetcurrencyid", nullable = false)
    private Currency targetCurrency;

    @Column(name = "rate")
    private BigDecimal rate;
}



