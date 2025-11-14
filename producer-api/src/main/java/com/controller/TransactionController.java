package com.controller;

import com.model.UserTransaction;
import com.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping("/send-transaction")
    int createTransaction(@RequestBody UserTransaction userTransaction) {
        return transactionService.initiateTransaction(userTransaction);
    }
}
