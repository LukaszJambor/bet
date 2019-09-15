package com.bookmarking.bet.queue;

import com.bookmarking.bet.dto.BetDto;
import com.bookmarking.bet.dto.ResultDto;
import com.bookmarking.bet.exception.EventEndedException;
import com.bookmarking.bet.service.MessageDeserializationService;
import com.bookmarking.bet.service.ProfitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class BetMessageReceiverTest {

    @InjectMocks
    private BetMessageReceiver betMessageReceiver;

    @Mock
    private MessageDeserializationService messageDeserializationService;

    @Mock
    private ProfitService profitService;

    @Test
    public void shouldEvaluateBetProfit_whenDataIsValid() throws IOException, EventEndedException {
        //given
        Mockito.when(messageDeserializationService.deserializeBet(anyString())).thenReturn(new BetDto());
        String message = "message";
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("message", message);

        //when
        betMessageReceiver.receiveBet(messageMap);

        //then
        verify(profitService, times(1)).evaluateBetProfit(any());
    }

    @Test
    public void shouldCatchException_whenBetMessageIsInvalid() throws IOException, EventEndedException {
        //given
        Mockito.when(messageDeserializationService.deserializeBet(anyString())).thenThrow(IOException.class);
        String message = "message";
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("message", message);

        //when
        betMessageReceiver.receiveBet(messageMap);

        //then
        verify(profitService, times(0)).evaluateBetProfit(any());
    }

    @Test
    public void shouldEvaluateResultProfit_whenDataIsValid() throws IOException, EventEndedException {
        //given
        Mockito.when(messageDeserializationService.deserializeResult(anyString())).thenReturn(new ResultDto());
        String message = "message";
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("message", message);

        //when
        betMessageReceiver.receiveResult(messageMap);

        //then
        verify(profitService, times(1)).evaluateResultProfit(any());
    }

    @Test
    public void shouldCatchException_whenResultMessageIsInvalid() throws IOException, EventEndedException {
        //given
        Mockito.when(messageDeserializationService.deserializeResult(anyString())).thenThrow(IOException.class);
        String message = "message";
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("message", message);

        //when
        betMessageReceiver.receiveResult(messageMap);

        //then
        verify(profitService, times(0)).evaluateResultProfit(any());
    }
}