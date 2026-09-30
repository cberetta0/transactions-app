package com.mendel.transactions_app.controller;

import com.mendel.transactions_app.dto.StatusResponse;
import com.mendel.transactions_app.dto.SumResponse;
import com.mendel.transactions_app.dto.TransactionRequest;
import com.mendel.transactions_app.service.TransactionServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionServiceImpl transactionService;

    public TransactionController(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }

    @PutMapping("/{id}")
    public StatusResponse putTransaction(@PathVariable Long id, @RequestBody TransactionRequest request){
        transactionService.save(id, request);
        return new StatusResponse("ok");
    }

    @GetMapping("/types/{type}")
    public List<Long> getTransactionsByType(@PathVariable String type) {
        return transactionService.getTransactionsByType(type);
    }

    @GetMapping("/sum/{transactionId}")
    public SumResponse getTotalAmount(@PathVariable Long transactionId) {
        return new SumResponse(transactionService.getTotalAmount(transactionId));
    }
}
