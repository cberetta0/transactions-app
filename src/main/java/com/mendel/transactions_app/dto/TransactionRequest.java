package com.mendel.transactions_app.dto;

public record TransactionRequest(Double amount, String type, Long parentId) { }
