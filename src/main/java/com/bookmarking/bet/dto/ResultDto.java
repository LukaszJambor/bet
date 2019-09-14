package com.bookmarking.bet.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
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
}