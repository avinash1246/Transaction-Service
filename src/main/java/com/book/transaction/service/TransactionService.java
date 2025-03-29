package com.book.transaction.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.transaction.model.ResponseMessage;
import com.book.transaction.model.Transaction;
import com.book.transaction.repository.TransactionRepository;

@Service
public class TransactionService {
	
	@Autowired
    private TransactionRepository transactionRepository;
	
    public ResponseMessage addTransaction(Transaction transaction, Map<String, Object> claimsMap) {
    	if(transaction.getTransactionType().equals("CREDIT") || transaction.getTransactionType().equals("DEBIT")) {
    		transaction.setUserId((String) claimsMap.get("userId"));
        	transaction.setUserEmailId((String) claimsMap.get("email"));
        	Transaction addedTransaction = transactionRepository.save(transaction);
            if(Objects.nonNull(addedTransaction))
    			return new ResponseMessage(200, "Transaction Success!");
    		else
    			return new ResponseMessage(400, "Transaction Failed!");
    	}else 
    		return new ResponseMessage(400, "Transaction type must be CREDIT or DEBIT");
    }

    public List<Transaction> getTransactionsByCustomer(String customerId, Map<String, Object> claimsMap) {
//        return transactionRepository.findByCustomerId(customerId);
        return transactionRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    public Map<String, BigDecimal> getCustomerBalance(String customerId, Map<String, Object> claimsMap) {
        List<Transaction> transactions = transactionRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);

        BigDecimal balance = BigDecimal.ZERO;
        for (Transaction t : transactions) {
            if (t.getTransactionType().equals("CREDIT")) {
                balance = balance.add(t.getTransactionAmount());
            } else if (t.getTransactionType().equals("DEBIT")){
                balance = balance.subtract(t.getTransactionAmount());
            }
        }
        return Map.of("balance", balance);
    }

	public List<Transaction> getTransactionsByUserId(Map<String, Object> claimsMap) {
		return transactionRepository.findByUserIdOrderByCreatedAtDesc((String) claimsMap.get("userId"));
	}
	
    public Map<String, BigDecimal> getTransactionSummary(Map<String, Object> claimsMap) {
    	Map<String, BigDecimal> summaryMap = new HashMap<>();
        List<Transaction> transactions = transactionRepository.findByUserIdOrderByCreatedAtDesc((String) claimsMap.get("userId"));

        BigDecimal youGaveBalance = BigDecimal.ZERO;
        BigDecimal youGetBalance = BigDecimal.ZERO;
        for (Transaction t : transactions) {
            if (t.getTransactionType().equals("CREDIT")) {
            	youGetBalance = youGetBalance.add(t.getTransactionAmount());
            } else if (t.getTransactionType().equals("DEBIT")){
            	youGaveBalance = youGaveBalance.subtract(t.getTransactionAmount());
            }
        }
        summaryMap.put("youGaveBalance", youGaveBalance);
        summaryMap.put("youGetBalance", youGetBalance);
        return summaryMap;
    }
}

