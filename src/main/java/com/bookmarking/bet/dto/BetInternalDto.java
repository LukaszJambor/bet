package com.bookmarking.bet.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class BetInternalDto {

    private String fixture;
    private Outcome outcome;
    private BigDecimal stake;
    private BigDecimal odds;
}
