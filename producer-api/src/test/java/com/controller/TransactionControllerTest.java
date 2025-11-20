package com.controller;

import com.model.UserTransaction;
import com.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("createTransaction")
    class CreateTransaction {

        @Test
        @DisplayName("Should return transaction ID when transaction is successfully initiated")
        void shouldReturnTransactionIdWhenTransactionIsSuccessfullyInitiated() {
            UserTransaction userTransaction = new UserTransaction(123, 100.0, "USD");
            when(transactionService.initiateTransaction(userTransaction)).thenReturn(1);

            int result = transactionController.createTransaction(userTransaction);

            assertEquals(1, result);
            verify(transactionService, times(1)).initiateTransaction(userTransaction);
        }

        @Test
        @DisplayName("Should handle null UserTransaction gracefully")
        void shouldHandleNullUserTransactionGracefully() {
            UserTransaction userTransaction = null;

            int result = transactionController.createTransaction(userTransaction);

            assertEquals(0, result);
            verify(transactionService, never()).initiateTransaction(any());
        }

        @Test
        @DisplayName("Should handle exception thrown by TransactionService")
        void shouldHandleExceptionThrownByTransactionService() {
            UserTransaction userTransaction = new UserTransaction(123, 100.0, "USD");
            when(transactionService.initiateTransaction(userTransaction)).thenThrow(new RuntimeException("Service error"));

            int result = transactionController.createTransaction(userTransaction);

            assertEquals(0, result);
            verify(transactionService, times(1)).initiateTransaction(userTransaction);
        }
    }
}
