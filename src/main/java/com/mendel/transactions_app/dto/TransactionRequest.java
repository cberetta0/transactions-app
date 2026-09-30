package com.mendel.transactions_app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionRequest(Double amount, String type, @JsonProperty("parent_id") Long parentId) { }
