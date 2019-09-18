package com.bookmarking.bet.service.impl;

import com.bookmarking.bet.dto.BetDto;
import com.bookmarking.bet.dto.BetInternalDto;
import com.bookmarking.bet.dto.ResultDto;
import com.bookmarking.bet.exception.EventEndedException;
import com.bookmarking.bet.service.ProfitService;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bookmarking.bet.dto.Outcome.DRAW;
import static com.bookmarking.bet.dto.Outcome.FIRST;

@Component
public class ProfitServiceImpl implements ProfitService {

    private List<BetDto> betDtoList;
    private List<ResultDto> resultDtoList;

    public ProfitServiceImpl() {
        this.betDtoList = new ArrayList<>();
        this.resultDtoList = new ArrayList<>();
    }

    public void evaluateBetProfit(final BetDto betDto) throws EventEndedException {
        Optional<ResultDto> result = findEndedEvent(betDto);
        if (result.isPresent()) {
            throw new EventEndedException("Event ended, bet is impossible");
        }
        setAllProfits(betDto);
        betDtoList.add(betDto);
        calculateActualProfit(betDto);
    }

    public void evaluateResultProfit(final ResultDto resultDto) {
        final BigDecimal profitFromEvent = getProfitFromEvent(resultDto);
        resultDto.setProfit(profitFromEvent);
        resultDtoList.add(resultDto);
        final BigDecimal overallBalance = getOverallBalance();
        System.out.println("Overall balance: " + overallBalance + resultDto.toString());
    }

    private void calculateActualProfit(BetDto betDto) {
        final BigDecimal first = calculateActualFirst(betDto);
        final BigDecimal draw = calculateActualDraw(betDto);
        final BigDecimal second = calculateActualSecond(betDto);
        System.out.println("1: " + first + " X: " + draw + " 2: " + second);
    }

    private BigDecimal calculateActualSecond(BetDto betDto) {
        return betDtoList.stream()
                .filter(bet -> betDto.getBet().getFixture().equals(bet.getBet().getFixture()))
                .map(BetDto::getSecondProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateActualDraw(BetDto betDto) {
        return betDtoList.stream()
                .filter(bet -> betDto.getBet().getFixture().equals(bet.getBet().getFixture()))
                .map(BetDto::getDrawProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateActualFirst(BetDto betDto) {
        return betDtoList.stream()
                .filter(bet -> betDto.getBet().getFixture().equals(bet.getBet().getFixture()))
                .map(BetDto::getFirstProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Optional<ResultDto> findEndedEvent(final BetDto betDto) {
        return resultDtoList.stream()
                .filter(resultDto -> betDto.getBet().getFixture().equals(resultDto.getResult().getFixture()))
                .findAny();
    }

    @VisibleForTesting
    protected BigDecimal getOverallBalance() {
        return resultDtoList.stream()
                .map(ResultDto::getProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getProfitFromEvent(final ResultDto resultDto) {
        return betDtoList.stream()
                .filter(betDto -> resultDto.getResult().getFixture().equals(betDto.getBet().getFixture()))
                .map(betDto -> getProfit(betDto, resultDto))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getProfit(final BetDto betDto, final ResultDto resultDto) {
        if (FIRST.equals(resultDto.getResult().getResult())) {
            return betDto.getFirstProfit();
        } else if (DRAW.equals(resultDto.getResult().getResult())) {
            return betDto.getDrawProfit();
        } else {
            return betDto.getSecondProfit();
        }
    }

    private void setAllProfits(BetDto betDto) {
        final BetInternalDto bet = betDto.getBet();
        switch (bet.getOutcome()) {
            case FIRST:
                betDto.setFirstProfit(getBetProfitOnCustomerWin(bet));
                betDto.setDrawProfit(bet.getStake());
                betDto.setSecondProfit(bet.getStake());
                break;
            case DRAW:
                betDto.setFirstProfit(bet.getStake());
                betDto.setDrawProfit(getBetProfitOnCustomerWin(bet));
                betDto.setSecondProfit(bet.getStake());
                break;
            case SECOND:
                betDto.setFirstProfit(bet.getStake());
                betDto.setDrawProfit(bet.getStake());
                betDto.setSecondProfit(getBetProfitOnCustomerWin(bet));
                break;
        }
    }

    private BigDecimal getBetProfitOnCustomerWin(final BetInternalDto actualBet) {
        final BigDecimal prize = (actualBet.getStake().multiply(actualBet.getOdds()));
        final BigDecimal betProfit = actualBet.getStake().subtract(prize);
        return betProfit.setScale(2, RoundingMode.HALF_UP);
    }
}