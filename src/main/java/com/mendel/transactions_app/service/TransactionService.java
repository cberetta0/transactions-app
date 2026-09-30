package com.mendel.transactions_app.service;

import com.mendel.transactions_app.dto.TransactionRequest;
import com.mendel.transactions_app.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction getTransactionById(Long id);
    Transaction save(Long id, TransactionRequest transaction);
    List<Long> getTransactionsByType(String type);
    Double getTotalAmount(Long transactionId);
}
