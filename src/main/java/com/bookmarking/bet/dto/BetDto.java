package com.bookmarking.bet.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
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
}
