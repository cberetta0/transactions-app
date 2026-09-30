package com.mendel.transactions_app.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {

    private Long id;
    private Double amount;
    private String type;
    private Long parentId;

    public Transaction(){}

    public Transaction(Long id, Double amount, String type, Long parentId) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.parentId = parentId;
    }
}
