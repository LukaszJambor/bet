package com.bookmarking.bet.dto;

import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Setter
public class BetDto {

    private MessageType type;
    private BetInternalDto bet;
    private BigDecimal firstProfit;
    private BigDecimal drawProfit;
    private BigDecimal secondProfit;

    @Override
    public String toString() {
        return "1: " + firstProfit +
                " X: " + drawProfit +
                " 2: " + secondProfit;
    }

    public MessageType getType() {
        return type;
    }

    public BetInternalDto getBet() {
        return bet;
    }

    public BigDecimal getFirstProfit() {
        return firstProfit.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getDrawProfit() {
        return drawProfit.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSecondProfit() {
        return secondProfit.setScale(2, RoundingMode.HALF_UP);
    }
}
