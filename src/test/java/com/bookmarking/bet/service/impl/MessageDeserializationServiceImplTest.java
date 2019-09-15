package com.bookmarking.bet.service.impl;

import com.bookmarking.bet.dto.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;

import static com.bookmarking.bet.dto.MessageType.BET;
import static com.bookmarking.bet.dto.MessageType.RESULT;
import static com.bookmarking.bet.dto.Outcome.DRAW;
import static com.bookmarking.bet.dto.Outcome.FIRST;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class MessageDeserializationServiceImplTest {

    @InjectMocks
    private MessageDeserializationServiceImpl messageDeserializationServiceImpl;

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
    }

    @Test
    public void shouldDeserializeBet_whenAllDataIsAvailable() throws IOException {
        //given
        String betJson = "{ \"type\" : \"BET\", \"bet\" : { \"fixture\" : \"Liverpool FC - FC Porto\", \"outcome\" : \"1\", \"stake\" : 20.00, \"odds\" : 1.35 } }";
        BetDto betDtoExample = createBetDto(BET, "Liverpool FC - FC Porto", new BigDecimal(1.35), FIRST, new BigDecimal(20.00));
        Mockito.when(objectMapper.readValue(betJson, BetDto.class)).thenReturn(betDtoExample);

        //when
        BetDto betDto = messageDeserializationServiceImpl.deserializeBet(betJson);

        //then
        assertThat(betDto).isNotNull();
        assertThat(betDto.getBet()).isNotNull();
        assertThat(betDto).isEqualTo(betDtoExample);
    }

    @Test(expected = JsonMappingException.class)
    public void shouldThrowJsonMappingException_whenBetTypeIsResult() throws IOException {
        //given
        String betJson = "{ \"type\" : \"RESULT\", \"bet\" : { \"fixture\" : \"Liverpool FC - FC Porto\", \"outcome\" : \"1\", \"stake\" : 20.00, \"odds\" : 1.35 } }";
        BetDto betDtoExample = createBetDto(RESULT, "Liverpool FC - FC Porto", new BigDecimal(1.35), FIRST, new BigDecimal(20.00));
        Mockito.when(objectMapper.readValue(betJson, BetDto.class)).thenReturn(betDtoExample);

        //when
        messageDeserializationServiceImpl.deserializeBet(betJson);

        //then
    }

    @Test(expected = JsonMappingException.class)
    public void shouldThrowJsonMappingException_whenBetTypeIsUnavailable() throws IOException {
        //given
        String betJson = "{ \"bet\" : { \"fixture\" : \"Liverpool FC - FC Porto\", \"outcome\" : \"1\", \"stake\" : 20.00, \"odds\" : 1.35 } }";
        BetDto betDtoExample = createBetDto(null, "Liverpool FC - FC Porto", new BigDecimal(1.35), FIRST, new BigDecimal(20.00));
        Mockito.when(objectMapper.readValue(betJson, BetDto.class)).thenReturn(betDtoExample);

        //when
        messageDeserializationServiceImpl.deserializeBet(betJson);

        //then
    }

    @Test(expected = JsonMappingException.class)
    public void shouldThrowJsonMappingException_whenFixtureIsUnavailable() throws IOException {
        //given
        String betJson = "{ \"type\" : \"BET\", \"bet\" : { \"outcome\" : \"1\", \"stake\" : 20.00, \"odds\" : 1.35 } }";
        BetDto betDtoExample = createBetDto(BET, null, new BigDecimal(1.35), FIRST, new BigDecimal(20.00));
        Mockito.when(objectMapper.readValue(betJson, BetDto.class)).thenReturn(betDtoExample);

        //when
        messageDeserializationServiceImpl.deserializeBet(betJson);

        //then
    }

    @Test(expected = JsonMappingException.class)
    public void shouldThrowJsonMappingException_whenOutcomeIsUnavailable() throws IOException {
        //given
        String betJson = "{ \"type\" : \"RESULT\", \"bet\" : { \"fixture\" : \"Liverpool FC - FC Porto\", \"stake\" : 20.00, \"odds\" : 1.35 } }";
        BetDto betDtoExample = createBetDto(BET, "Liverpool FC - FC Porto", new BigDecimal(1.35), null, new BigDecimal(20.00));
        Mockito.when(objectMapper.readValue(betJson, BetDto.class)).thenReturn(betDtoExample);

        //when
        messageDeserializationServiceImpl.deserializeBet(betJson);

        //then
    }

    @Test(expected = JsonMappingException.class)
    public void shouldThrowJsonMappingException_whenStakeIsUnavailable() throws IOException {
        //given
        String betJson = "{ \"type\" : \"BET\", \"bet\" : { \"fixture\" : \"Liverpool FC - FC Porto\", \"outcome\" : \"1\", \"odds\" : 1.35 } }";
        BetDto betDtoExample = createBetDto(BET, "Liverpool FC - FC Porto", new BigDecimal(1.35), FIRST, null);
        Mockito.when(objectMapper.readValue(betJson, BetDto.class)).thenReturn(betDtoExample);

        //when
        messageDeserializationServiceImpl.deserializeBet(betJson);

        //then
    }

    @Test(expected = JsonMappingException.class)
    public void shouldThrowJsonMappingException_whenOddsIsUnavailable() throws IOException {
        //given
        String betJson = "{ \"type\" : \"BET\", \"bet\" : { \"fixture\" : \"Liverpool FC - FC Porto\", \"outcome\" : \"1\", \"stake\" : 20.00 } }";
        BetDto betDtoExample = createBetDto(BET, "Liverpool FC - FC Porto", null, FIRST, new BigDecimal(20.00));
        Mockito.when(objectMapper.readValue(betJson, BetDto.class)).thenReturn(betDtoExample);

        //when
        messageDeserializationServiceImpl.deserializeBet(betJson);

        //then
    }

    @Test
    public void shouldDeserializeResult_whenAllDataIsAvailable() throws IOException {
        //given
        String resultJson = "{ \"type\" : \"RESULT\", \"result\" : { \"fixture\" : \"Liverpool FC - FC Porto\", \"result\" : \"X\" } }";
        ResultDto resultDtoExample = createResultDto(RESULT, "Liverpool FC - FC Porto", DRAW);
        Mockito.when(objectMapper.readValue(resultJson, ResultDto.class)).thenReturn(resultDtoExample);

        //when
        ResultDto resultDto = messageDeserializationServiceImpl.deserializeResult(resultJson);

        //then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getResult()).isNotNull();
        assertThat(resultDto).isEqualTo(resultDtoExample);
    }

    @Test(expected = JsonMappingException.class)
    public void shouldDeserializeResult_whenTypeIsBet() throws IOException {
        //given
        String resultJson = "{ \"type\" : \"BET\", \"result\" : { \"fixture\" : \"Liverpool FC - FC Porto\", \"result\" : \"X\" } }";
        ResultDto resultDtoExample = createResultDto(BET, "Liverpool FC - FC Porto", DRAW);
        Mockito.when(objectMapper.readValue(resultJson, ResultDto.class)).thenReturn(resultDtoExample);

        //when
        ResultDto resultDto = messageDeserializationServiceImpl.deserializeResult(resultJson);

        //then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getResult()).isNotNull();
        assertThat(resultDto).isEqualTo(resultDtoExample);
    }

    @Test(expected = JsonMappingException.class)
    public void shouldDeserializeResult_whenTypeIsUnavailable() throws IOException {
        //given
        String resultJson = "{ \"result\" : { \"fixture\" : \"Liverpool FC - FC Porto\", \"result\" : \"X\" } }";
        ResultDto resultDtoExample = createResultDto(null, "Liverpool FC - FC Porto", DRAW);
        Mockito.when(objectMapper.readValue(resultJson, ResultDto.class)).thenReturn(resultDtoExample);

        //when
        ResultDto resultDto = messageDeserializationServiceImpl.deserializeResult(resultJson);

        //then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getResult()).isNotNull();
        assertThat(resultDto).isEqualTo(resultDtoExample);
    }

    @Test(expected = JsonMappingException.class)
    public void shouldDeserializeResult_whenFixtureIsUnavailable() throws IOException {
        //given
        String resultJson = "{ \"type\" : \"RESULT\", \"result\" : { \"result\" : \"X\" } }";
        ResultDto resultDtoExample = createResultDto(BET, null, DRAW);
        Mockito.when(objectMapper.readValue(resultJson, ResultDto.class)).thenReturn(resultDtoExample);

        //when
        ResultDto resultDto = messageDeserializationServiceImpl.deserializeResult(resultJson);

        //then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getResult()).isNotNull();
        assertThat(resultDto).isEqualTo(resultDtoExample);
    }

    @Test(expected = JsonMappingException.class)
    public void shouldDeserializeResult_whenEventResultIsNull() throws IOException {
        //given
        String resultJson = "{ \"type\" : \"RESULT\", \"result\" : { \"fixture\" : \"Liverpool FC - FC Porto\" } }";
        ResultDto resultDtoExample = createResultDto(BET, "Liverpool FC - FC Porto", null);
        Mockito.when(objectMapper.readValue(resultJson, ResultDto.class)).thenReturn(resultDtoExample);

        //when
        ResultDto resultDto = messageDeserializationServiceImpl.deserializeResult(resultJson);

        //then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getResult()).isNotNull();
        assertThat(resultDto).isEqualTo(resultDtoExample);
    }

    private BetDto createBetDto(MessageType messageType, String fixture, BigDecimal odds, Outcome outcome, BigDecimal stake) {
        BetDto betDto = new BetDto();
        BetInternalDto betInternalDto = new BetInternalDto();
        betInternalDto.setFixture(fixture);
        betInternalDto.setOdds(odds);
        betInternalDto.setOutcome(outcome);
        betInternalDto.setStake(stake);
        betDto.setType(messageType);
        betDto.setBet(betInternalDto);
        return betDto;
    }

    private ResultDto createResultDto(MessageType messageType, String fixture, Outcome outcome) {
        ResultDto resultDto = new ResultDto();
        ResultInternalDto resultInternalDto = new ResultInternalDto();
        resultDto.setType(messageType);
        resultInternalDto.setFixture(fixture);
        resultInternalDto.setResult(outcome);
        resultDto.setResult(resultInternalDto);
        return resultDto;
    }
}