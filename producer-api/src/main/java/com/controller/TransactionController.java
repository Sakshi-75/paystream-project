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
        if (userTransaction == null) {
            return 0;
        }
        try {
            return transactionService.initiateTransaction(userTransaction);
        } catch (Exception e) {
            return 0;
        }
    }
}
