package com.book.transaction.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.transaction.model.ResponseMessage;
import com.book.transaction.model.Transaction;
import com.book.transaction.security.JwtUtil;
import com.book.transaction.service.TransactionService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/addTransaction")
    public ResponseEntity<ResponseMessage> addTransaction(@RequestBody Transaction transaction,
    		HttpServletRequest request) {
        Map<String, Object> claimsMap = getClaims(request);
        return ResponseEntity.ok(transactionService.addTransaction(transaction, claimsMap));
    }
    
    @GetMapping("/getTransactions/{customerId}")
    public ResponseEntity<List<Transaction>> getTransactionsByCustomer(@PathVariable String customerId, HttpServletRequest request) {
        Map<String, Object> claimsMap = getClaims(request);
        return ResponseEntity.ok(transactionService.getTransactionsByCustomer(customerId, claimsMap));
    }

    @GetMapping("/balance/{customerId}")
    public ResponseEntity<Map<String, BigDecimal>> getBalance(@PathVariable String customerId, HttpServletRequest request) {
    	Map<String, Object> claimsMap = getClaims(request);
        return ResponseEntity.ok(transactionService.getCustomerBalance(customerId, claimsMap));
    }

    @GetMapping("/myTransactions")
    public ResponseEntity<List<Transaction>> getMyTransactions(HttpServletRequest request) {
    	Map<String, Object> claimsMap = getClaims(request);
        return ResponseEntity.ok(transactionService.getTransactionsByEmail(claimsMap));
    }
    
    @GetMapping("/transactionSummary")
    public ResponseEntity<Map<String, BigDecimal>> transactionSummary(HttpServletRequest request) {
    	Map<String, Object> claimsMap = getClaims(request);
        return ResponseEntity.ok(transactionService.getTransactionSummary(claimsMap));
    }
    
	private Map<String, Object> getClaims(HttpServletRequest request) {
		Map<String, Object> claimsMap = new HashMap<>();
        String token = jwtUtil.extractToken(request);
        String email = jwtUtil.extractEmail(token);
        String userId = jwtUtil.extractUserId(token);
        claimsMap.put("email", email);
        claimsMap.put("userId", userId);
        System.out.println("claimsMap--"+claimsMap);
		return claimsMap;
	}
}
