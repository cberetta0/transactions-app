package com.mendel.transactions_app.service;

import com.mendel.transactions_app.dto.TransactionRequest;
import com.mendel.transactions_app.model.Transaction;
import com.mendel.transactions_app.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public Transaction save(Long id, TransactionRequest request) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setParentId(request.getParentId());
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Long> getTransactionsByType(String type) {
        return transactionRepository.findByType(type).stream()
                .map(Transaction::getId)
                .toList();
    }

    @Override
    public Double getTotalAmount(Long transactionId) {
        return calculateSum(transactionId);
    }

    private double calculateSum(Long transactionId) {
        Transaction transaction = getTransactionById(transactionId);

        double total = transaction.getAmount();

        for (Transaction child :
                transactionRepository.findByParentId(transactionId)) {
            total += calculateSum(child.getId());
        }

        return total;
    }
}