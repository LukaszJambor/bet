package com.bookmarking.bet.queue;

import com.bookmarking.bet.dto.BetDto;
import com.bookmarking.bet.dto.ResultDto;
import com.bookmarking.bet.exception.EventEndedException;
import com.bookmarking.bet.service.MessageDeserializationService;
import com.bookmarking.bet.service.ProfitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class BetMessageReceiver {

    private Logger logger = LoggerFactory.getLogger(BetMessageReceiver.class);
    private static final String SEND_BET_MESSAGE_QUEUE = "send.bet.message.queue";
    private static final String SEND_RESULT_MESSAGE_QUEUE = "send.result.message.queue";
    private static final String MESSAGE = "message";

    private MessageDeserializationService messageDeserializationService;
    private ProfitService profitService;

    public BetMessageReceiver(final MessageDeserializationService messageDeserializationService, final ProfitService profitService) {
        this.messageDeserializationService = messageDeserializationService;
        this.profitService = profitService;
    }

    @JmsListener(destination = SEND_BET_MESSAGE_QUEUE)
    public void receiveBet(final Map<String, String> map) {
        String message = map.get(MESSAGE);
        try {
            final BetDto betDto = messageDeserializationService.deserializeBet(message);
            profitService.evaluateBetProfit(betDto);
        } catch (IOException | EventEndedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @JmsListener(destination = SEND_RESULT_MESSAGE_QUEUE)
    public void receiveResult(final Map<String, String> map) {
        String message = map.get(MESSAGE);
        try {
            final ResultDto resultDto = messageDeserializationService.deserializeResult(message);
            profitService.evaluateResultProfit(resultDto);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}