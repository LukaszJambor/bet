package com.bookmarking.bet.service;

import com.bookmarking.bet.dto.BetDto;
import com.bookmarking.bet.dto.ResultDto;

import java.io.IOException;

public interface MessageDeserializationService {

    /**
     * Deserialize message to BetDto
     * @param message
     * @return
     * @throws IOException
     */
    BetDto deserializeBet(final String message) throws IOException;

    /**
     * Deserialize message to ResultDto
     * @param message
     * @return
     * @throws IOException
     */
    ResultDto deserializeResult(final String message) throws IOException;
}
