package com.bookmarking.bet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Outcome {
    @JsonProperty("1")
    FIRST,

    @JsonProperty("X")
    DRAW,

    @JsonProperty("2")
    SECOND
}