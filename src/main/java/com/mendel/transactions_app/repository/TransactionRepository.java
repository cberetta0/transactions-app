package com.mendel.transactions_app.repository;

import com.mendel.transactions_app.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TransactionRepository {

    private final Map<Long, Transaction> transactionMap = new HashMap<>();

    public Transaction save(Transaction transaction){
        return transactionMap.put(transaction.getId(), transaction);
    }

    public Optional<Transaction> findById(Long id){
        return Optional.ofNullable(transactionMap.get(id));
    }

    public List<Transaction> findByType(String type){
        return transactionMap.values().stream()
                .filter(transaction -> transaction.getType().equals(type))
                .toList();
    }

    public List<Transaction> findByParentId(Long parentId){
        return transactionMap.values().stream()
                .filter(transaction -> parentId.equals(transaction.getParentId()))
                .toList();
    }
}
