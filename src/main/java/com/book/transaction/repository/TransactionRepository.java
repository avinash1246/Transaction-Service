package com.book.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.book.transaction.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByCustomerId(String customerId);
    List<Transaction> findByUserId(String userId);
}
