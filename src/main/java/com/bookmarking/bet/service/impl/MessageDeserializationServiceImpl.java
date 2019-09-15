package com.bookmarking.bet.service.impl;

import com.bookmarking.bet.dto.BetDto;
import com.bookmarking.bet.dto.MessageType;
import com.bookmarking.bet.dto.ResultDto;
import com.bookmarking.bet.service.MessageDeserializationService;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageDeserializationServiceImpl implements MessageDeserializationService {

    private ObjectMapper objectMapper;

    public MessageDeserializationServiceImpl(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public BetDto deserializeBet(final String message) throws IOException {
        final BetDto betDto = objectMapper.readValue(message, BetDto.class);
        if (checkBetMandatoryFieldsAvailability(betDto)) {
            return betDto;
        } else {
            throw new JsonMappingException("Incomplete JSON: " + message);
        }
    }

    public ResultDto deserializeResult(final String message) throws IOException {
        final ResultDto resultDto = objectMapper.readValue(message, ResultDto.class);
        if (checkResultMandatoryFieldsAvailability(resultDto)) {
            return resultDto;
        } else {
            throw new JsonMappingException("Incomplete JSON: " + message);
        }
    }

    private boolean checkResultMandatoryFieldsAvailability(ResultDto resultDto) {
        return resultDto.getType() != null && MessageType.RESULT.equals(resultDto.getType()) && resultDto.getResult() != null
                && resultDto.getResult().getFixture() != null && resultDto.getResult().getResult() != null;
    }

    private boolean checkBetMandatoryFieldsAvailability(BetDto betDto) {
        return betDto.getType() != null && MessageType.BET.equals(betDto.getType()) && betDto.getBet() != null && betDto.getBet().getFixture() != null
                && betDto.getBet().getOdds() != null && betDto.getBet().getOutcome() != null && betDto.getBet().getStake() != null;
    }
}
