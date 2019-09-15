package com.bookmarking.bet.dto;

import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Setter
public class BetInternalDto {

    private String fixture;
    private Outcome outcome;
    private BigDecimal stake;
    private BigDecimal odds;

    public String getFixture() {
        return fixture;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public BigDecimal getStake() {
        return stake.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getOdds() {
        return odds.setScale(2, RoundingMode.HALF_UP);
    }
}
