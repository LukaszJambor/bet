package com.bookmarking.bet.dto;

import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Setter
public class ResultDto {

    private MessageType type;
    private ResultInternalDto result;
    private BigDecimal profit;

    @Override
    public String toString() {
        return " Fixture " +
                result.getFixture() +
                " profit: " +
                profit;
    }

    public BigDecimal getProfit() {
        return profit.setScale(2, RoundingMode.HALF_UP);
    }

    public MessageType getType() {
        return type;
    }

    public ResultInternalDto getResult() {
        return result;
    }
}