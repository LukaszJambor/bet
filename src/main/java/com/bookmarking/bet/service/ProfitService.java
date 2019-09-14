package com.bookmarking.bet.service;

import com.bookmarking.bet.dto.BetDto;
import com.bookmarking.bet.dto.ResultDto;
import com.bookmarking.bet.exception.EventEndedException;

public interface ProfitService {

    /**
     * Evaluate bet profit and store into list.
     * @param betDto
     * @throws EventEndedException
     */
    void evaluateBetProfit(final BetDto betDto) throws EventEndedException;

    /**
     * Evaluate event result and store into list.
     * @param resultDto
     */
    void evaluateResultProfit(final ResultDto resultDto);
}
