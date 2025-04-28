package com.atmapplication.AtmApplication.Controller;


import com.atmapplication.AtmApplication.Model.Transaction;
import com.atmapplication.AtmApplication.Model.User;
import com.atmapplication.AtmApplication.Service.AtmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atm")
public class AtmController {

    private final AtmService atmService;

    public AtmController(AtmService atmService) {
        this.atmService = atmService;
    }

    // Create a new user
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = atmService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(atmService.getAllUsers());
    }

    // Credit money using account number
    @PutMapping("/credit/{accountNumber}")
    public ResponseEntity<User> creditMoney(@PathVariable String accountNumber, @RequestParam Double amount) {
        return ResponseEntity.ok(atmService.creditMoney(accountNumber, amount));


    }

    // Withdraw money using account number
    @PutMapping("/withdraw/{accountNumber}")
    public ResponseEntity<String> withdrawMoney(@PathVariable String accountNumber, @RequestParam Double amount) {
        String message = atmService.withdrawMoney(accountNumber, amount);
        return ResponseEntity.ok(message);
    }

    // Change PIN using account number
    @PutMapping("/changepin/{accountNumber}")
    public ResponseEntity<User> changePin(@PathVariable String accountNumber, @RequestParam String newPin) {
        return ResponseEntity.ok(atmService.changePin(accountNumber, newPin));
    }

    // Transfer funds between two accounts
    @PutMapping("/transfer")
    public ResponseEntity<User> transferFunds(
            @RequestParam String senderAccountNumber,
            @RequestParam String receiverAccountNumber,
            @RequestParam Double amount)
    {
        return ResponseEntity.ok(atmService.transferFunds(senderAccountNumber, receiverAccountNumber, amount));
    }


    @GetMapping("/users/{accountNumber}")
    public ResponseEntity<User> getUserByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(atmService.getUserByAccountNumber(accountNumber));
    }


    // Check balance using account number
    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<Double> checkBalance(@PathVariable String accountNumber) {
        return ResponseEntity.ok(atmService.checkBalance(accountNumber));
    }

    @GetMapping("/transactions/all/{accountNumber}")
    public ResponseEntity<List<Transaction>> getAllTransactions(@PathVariable String accountNumber) {
        return ResponseEntity.ok(atmService.getAllTransactions(accountNumber));
    }



}
