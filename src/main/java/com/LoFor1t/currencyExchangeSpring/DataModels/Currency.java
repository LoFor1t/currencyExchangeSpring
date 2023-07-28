package com.LoFor1t.currencyExchangeSpring.DataModels;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "currencies")
@NoArgsConstructor
@Getter
@Setter
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "sign")
    private String sign;

    public Currency(String code, String fullName, String sign) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }
}
