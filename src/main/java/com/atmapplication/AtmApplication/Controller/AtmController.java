package com.atmapplication.AtmApplication.Controller;

import jakarta.validation.Valid;
import com.atmapplication.AtmApplication.Model.Transaction;
import com.atmapplication.AtmApplication.Model.User;
import com.atmapplication.AtmApplication.Service.AtmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/atm")
public class AtmController {

    private final AtmService atmService;

    public AtmController(AtmService atmService) {
        this.atmService = atmService;
    }

    // Create a new user
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = atmService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(atmService.getAllUsers());
    }

    // Credit money using account number
    @PutMapping("/credit/{cardNumber}")
    public ResponseEntity<User> creditMoney(@PathVariable String cardNumber, @RequestParam Double amount) {
        return ResponseEntity.ok(atmService.creditMoney(cardNumber, amount));


    }

    // Withdraw money using account number
    @PutMapping("/withdraw/{cardNumber}")
    public ResponseEntity<String> withdrawMoney(
            @PathVariable String cardNumber,
            @RequestParam Double amount,
            @RequestParam String accountType) {
        String message = atmService.withdrawMoney(cardNumber, amount, accountType);
        return ResponseEntity.ok(message);
    }

    // Change PIN using account number
    @PutMapping("/changepin/{cardNumber}")
    public ResponseEntity<User> changePin(@PathVariable String cardNumber, @RequestParam String newPin) {
        return ResponseEntity.ok(atmService.changePin(cardNumber, newPin));
    }

    // Transfer funds between two accounts
    @PutMapping("/transfer")
    public ResponseEntity<User> transferFunds(
            @RequestParam String senderCardNumber,
            @RequestParam String receiverCardNumber,
            @RequestParam Double amount)
    {
        return ResponseEntity.ok(atmService.transferFunds(senderCardNumber, receiverCardNumber, amount));
    }


    @GetMapping("/users/{cardNumber}")
    public ResponseEntity<User> getUserByCardNumber(@PathVariable String cardNumber) {
        return ResponseEntity.ok(atmService.getUserByCardNumber(cardNumber));
    }


    // Check balance using account number
    @GetMapping("/initialDeposit/{cardNumber}")
    public ResponseEntity<Double> checkBalance(@PathVariable String cardNumber) {
        return ResponseEntity.ok(atmService.checkBalance(cardNumber));
    }

    @GetMapping("/transactions/all/{cardNumber}")
    public ResponseEntity<List<Transaction>> getAllTransactions(@PathVariable String cardNumber) {
        return ResponseEntity.ok(atmService.getAllTransactions(cardNumber));
    }



}
