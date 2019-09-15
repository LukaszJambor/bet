package com.bookmarking.bet.service.impl;

import com.bookmarking.bet.dto.*;
import com.bookmarking.bet.exception.EventEndedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static com.bookmarking.bet.dto.MessageType.BET;
import static com.bookmarking.bet.dto.MessageType.RESULT;
import static com.bookmarking.bet.dto.Outcome.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ProfitServiceImplTest {

    @InjectMocks
    private ProfitServiceImpl profitService;

    private BetDto bet1, bet2, bet3, bet4, bet5, bet6, bet7, bet8, bet9, bet10, bet11;
    private ResultDto result1, result2, result3;

    @Before
    public void setUp() {
        bet1 = createBetDto(BET, "Liverpool FC - FC Porto", FIRST, new BigDecimal(20.00), new BigDecimal(1.35));
        bet2 = createBetDto(BET, "Liverpool FC - FC Porto", DRAW, new BigDecimal(10.50), new BigDecimal(4.55));
        bet3 = createBetDto(BET, "Ajax - Juventus", DRAW, new BigDecimal(5.50), new BigDecimal(3.75));
        bet4 = createBetDto(BET, "Liverpool FC - FC Porto", SECOND, new BigDecimal(3.20), new BigDecimal(7.43));
        bet5 = createBetDto(BET, "Ajax - Juventus", DRAW, new BigDecimal(24.00), new BigDecimal(3.79));
        bet6 = createBetDto(BET, "Liverpool FC - FC Porto", DRAW, new BigDecimal(31.40), new BigDecimal(4.53));
        bet7 = createBetDto(BET, "Ajax - Juventus", FIRST, new BigDecimal(42.75), new BigDecimal(1.77));
        bet8 = createBetDto(BET, "Tottenham - Manchester City", DRAW, new BigDecimal(15.10), new BigDecimal(2.85));
        bet9 = createBetDto(BET, "Liverpool FC - FC Porto", DRAW, new BigDecimal(100.00), new BigDecimal(4.54));
        bet10 = createBetDto(BET, "Ajax - Juventus", FIRST, new BigDecimal(17.30), new BigDecimal(1.76));
        bet11 = createBetDto(BET, "Ajax - Juventus", FIRST, new BigDecimal(2.50), new BigDecimal(1.65));
        result1 = createResultDto(RESULT, "Liverpool FC - FC Porto", DRAW);
        result2 = createResultDto(RESULT, "Manchester United - Barcelona", SECOND);
        result3 = createResultDto(RESULT, "Ajax - Juventus", SECOND);
    }

    @Test
    public void shouldReturnApprovedValues() throws EventEndedException {
        //given

        //when
        profitService.evaluateBetProfit(bet1);
        profitService.evaluateBetProfit(bet2);
        profitService.evaluateBetProfit(bet3);
        profitService.evaluateBetProfit(bet4);
        profitService.evaluateBetProfit(bet5);
        profitService.evaluateBetProfit(bet6);
        profitService.evaluateResultProfit(result1);
        profitService.evaluateBetProfit(bet7);
        profitService.evaluateBetProfit(bet8);
        try {
            profitService.evaluateBetProfit(bet9);
        } catch (EventEndedException e) {
            e.printStackTrace();
        }
        profitService.evaluateBetProfit(bet10);
        profitService.evaluateResultProfit(result2);
        profitService.evaluateBetProfit(bet11);
        profitService.evaluateResultProfit(result3);

        //then
        assertThat(bet1.getFirstProfit()).isEqualTo(BigDecimal.valueOf(-7.00).setScale(2));
        assertThat(bet1.getDrawProfit()).isEqualTo(BigDecimal.valueOf(20.00).setScale(2));
        assertThat(bet1.getSecondProfit()).isEqualTo(BigDecimal.valueOf(20.00).setScale(2));
        assertThat(bet2.getFirstProfit()).isEqualTo(BigDecimal.valueOf(3.50).setScale(2));
        assertThat(bet2.getDrawProfit()).isEqualTo(BigDecimal.valueOf(-17.28).setScale(2));
        assertThat(bet2.getSecondProfit()).isEqualTo(BigDecimal.valueOf(30.50).setScale(2));
        assertThat(bet3.getFirstProfit()).isEqualTo(BigDecimal.valueOf(9.00).setScale(2));
        assertThat(bet3.getDrawProfit()).isEqualTo(BigDecimal.valueOf(-32.41).setScale(2));
        assertThat(bet3.getSecondProfit()).isEqualTo(BigDecimal.valueOf(36.00).setScale(2));
        assertThat(bet4.getFirstProfit()).isEqualTo(BigDecimal.valueOf(12.20).setScale(2));
        assertThat(bet4.getDrawProfit()).isEqualTo(BigDecimal.valueOf(-29.21).setScale(2));
        assertThat(bet4.getSecondProfit()).isEqualTo(BigDecimal.valueOf(15.42).setScale(2));
        assertThat(bet5.getFirstProfit()).isEqualTo(BigDecimal.valueOf(36.20).setScale(2));
        assertThat(bet5.getDrawProfit()).isEqualTo(BigDecimal.valueOf(-96.17).setScale(2));
        assertThat(bet5.getSecondProfit()).isEqualTo(BigDecimal.valueOf(39.42).setScale(2));
        assertThat(bet6.getFirstProfit()).isEqualTo(BigDecimal.valueOf(67.60).setScale(2));
        assertThat(bet6.getDrawProfit()).isEqualTo(BigDecimal.valueOf(-207.01).setScale(2));
        assertThat(bet6.getSecondProfit()).isEqualTo(BigDecimal.valueOf(70.82).setScale(2));
        assertThat(bet7.getFirstProfit()).isEqualTo(BigDecimal.valueOf(34.68).setScale(2));
        assertThat(bet7.getDrawProfit()).isEqualTo(BigDecimal.valueOf(-164.26).setScale(2));
        assertThat(bet7.getSecondProfit()).isEqualTo(BigDecimal.valueOf(113.57).setScale(2));
        assertThat(bet8.getFirstProfit()).isEqualTo(BigDecimal.valueOf(49.78).setScale(2));
        assertThat(bet8.getDrawProfit()).isEqualTo(BigDecimal.valueOf(-192.20).setScale(2));
        assertThat(bet8.getSecondProfit()).isEqualTo(BigDecimal.valueOf(128.67).setScale(2));
        assertThat(bet10.getFirstProfit()).isEqualTo(BigDecimal.valueOf(36.63).setScale(2));
        assertThat(bet10.getDrawProfit()).isEqualTo(BigDecimal.valueOf(-174.90).setScale(2));
        assertThat(bet10.getSecondProfit()).isEqualTo(BigDecimal.valueOf(145.97).setScale(2));
        assertThat(bet11.getFirstProfit()).isEqualTo(BigDecimal.valueOf(35).setScale(2));
        assertThat(bet11.getDrawProfit()).isEqualTo(BigDecimal.valueOf(-172.40).setScale(2));
        assertThat(bet11.getSecondProfit()).isEqualTo(BigDecimal.valueOf(148.47).setScale(2));
        assertThat(result1.getProfit()).isEqualTo(BigDecimal.valueOf(-233.50).setScale(2));
        assertThat(result2.getProfit()).isEqualTo(BigDecimal.valueOf(0).setScale(2));
        assertThat(result3.getProfit()).isEqualTo(BigDecimal.valueOf(483.43).setScale(2));
    }

    @Test
    public void shouldReturnApprovedOverallBalance() throws EventEndedException {
        //given
        profitService.evaluateBetProfit(bet1);
        profitService.evaluateBetProfit(bet2);
        profitService.evaluateBetProfit(bet3);
        profitService.evaluateBetProfit(bet4);
        profitService.evaluateBetProfit(bet5);
        profitService.evaluateBetProfit(bet6);
        profitService.evaluateResultProfit(result1);
        profitService.evaluateBetProfit(bet7);
        profitService.evaluateBetProfit(bet8);
        try {
            profitService.evaluateBetProfit(bet9);
        } catch (EventEndedException e) {
            e.printStackTrace();
        }
        profitService.evaluateBetProfit(bet10);
        profitService.evaluateResultProfit(result2);
        profitService.evaluateBetProfit(bet11);
        profitService.evaluateResultProfit(result3);

        //when
        BigDecimal overallBalance = profitService.getOverallBalance();

        //then
        assertThat(overallBalance).isEqualTo(BigDecimal.valueOf(249.93).setScale(2));
    }

    private BetDto createBetDto(MessageType messageType, String fixture, Outcome outcome, BigDecimal stake, BigDecimal odds) {
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