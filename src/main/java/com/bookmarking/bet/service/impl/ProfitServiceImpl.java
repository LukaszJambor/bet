package com.bookmarking.bet.service.impl;

import com.bookmarking.bet.dto.BetDto;
import com.bookmarking.bet.dto.BetInternalDto;
import com.bookmarking.bet.dto.Outcome;
import com.bookmarking.bet.dto.ResultDto;
import com.bookmarking.bet.exception.EventEndedException;
import com.bookmarking.bet.service.ProfitService;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bookmarking.bet.dto.Outcome.*;

@Component
public class ProfitServiceImpl implements ProfitService {

    private Logger logger = LoggerFactory.getLogger(ProfitServiceImpl.class);
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
        System.out.println(betDto);
    }

    public void evaluateResultProfit(final ResultDto resultDto) {
        final BigDecimal profitFromEvent = getProfitFromEvent(resultDto);
        resultDto.setProfit(profitFromEvent);
        resultDtoList.add(resultDto);
        final BigDecimal overallBalance = getOverallBalance();
        System.out.println("Overall balance: " + overallBalance + resultDto.toString());
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
                betDto.setFirstProfit(getBetProfitOnCustomerWin(bet, FIRST));
                betDto.setDrawProfit(getBetProfitOnCustomerLoose(bet, DRAW));
                betDto.setSecondProfit(getBetProfitOnCustomerLoose(bet, SECOND));
                break;
            case DRAW:
                betDto.setFirstProfit(getBetProfitOnCustomerLoose(bet, FIRST));
                betDto.setDrawProfit(getBetProfitOnCustomerWin(bet, DRAW));
                betDto.setSecondProfit(getBetProfitOnCustomerLoose(bet, SECOND));
                break;
            case SECOND:
                betDto.setFirstProfit(getBetProfitOnCustomerLoose(bet, FIRST));
                betDto.setDrawProfit(getBetProfitOnCustomerLoose(bet, DRAW));
                betDto.setSecondProfit(getBetProfitOnCustomerWin(bet, SECOND));
                break;
        }
    }

    private BigDecimal getBetProfitOnCustomerLoose(final BetInternalDto bet, final Outcome outcome) {
        if (CollectionUtils.isEmpty(betDtoList)) {
            return bet.getStake();
        } else {
            return getSummaryBetProfit(outcome, bet.getStake());
        }
    }

    private BigDecimal getBetProfitOnCustomerWin(final BetInternalDto actualBet, final Outcome outcome) {
        final BigDecimal prize = (actualBet.getStake().multiply(actualBet.getOdds()));
        final BigDecimal betProfit = actualBet.getStake().subtract(prize);
        if (CollectionUtils.isEmpty(betDtoList)) {
            return betProfit.setScale(2, RoundingMode.HALF_UP);
        } else {
            return getSummaryBetProfit(outcome, betProfit).setScale(2, RoundingMode.HALF_UP);
        }
    }

    private BigDecimal getSummaryBetProfit(final Outcome outcome, final BigDecimal betProfit) {
        final BetDto lastBetDto = betDtoList.get(betDtoList.size() - 1);
        BigDecimal finalProfit;
        switch (outcome) {
            case FIRST:
                finalProfit = lastBetDto.getFirstProfit().add(betProfit.setScale(2, RoundingMode.HALF_UP));
                break;
            case DRAW:
                finalProfit = lastBetDto.getDrawProfit().add(betProfit.setScale(2, RoundingMode.HALF_UP));
                break;
            case SECOND:
                finalProfit = lastBetDto.getSecondProfit().add(betProfit.setScale(2, RoundingMode.HALF_UP));
                break;
            default:
                logger.error("Unexpected value: " + outcome);
                throw new IllegalStateException("Unexpected value: " + outcome);
        }
        return finalProfit;
    }
}