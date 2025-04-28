package com.atmapplication.AtmApplication.Service;

import com.atmapplication.AtmApplication.Exception.UserNotFoundException;
import com.atmapplication.AtmApplication.Model.Transaction;
import com.atmapplication.AtmApplication.Model.User;
import com.atmapplication.AtmApplication.Repository.TransactionRepository;
import com.atmapplication.AtmApplication.Repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AtmService {

    private final UserRepository userRepo;
    private final TransactionRepository transactionRepo;

    public AtmService(UserRepository userRepo, TransactionRepository transactionRepo) {
        this.userRepo = userRepo;
        this.transactionRepo = transactionRepo;
    }

    public User creditMoney(String accountNumber, Double amount) {
        User user = getUserByAccountNumber(accountNumber);
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        user.setBalance(user.getBalance() + amount);
        transactionRepo.save(new Transaction(accountNumber, "CREDIT", amount));
        return userRepo.save(user);
    }

    public String withdrawMoney(String accountNumber, Double amount) {
        User user = getUserByAccountNumber(accountNumber);
        double remainingBalance = user.getBalance() - amount;

        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }

        if (remainingBalance < 500) {
            throw new IllegalArgumentException("The amount you entered will exceed the minimum balance limit. A penalty will be charged or transaction blocked.");
        }

        user.setBalance(remainingBalance);
        userRepo.save(user);
        // Log transaction
        transactionRepo.save(new Transaction(accountNumber, "DEBIT", amount));

        return "Money withdrawn successfully. Please collect your cash.";
    }

    public User changePin(String accountNumber, String newPin) {
        User user = getUserByAccountNumber(accountNumber);
        user.setPin(newPin);
        return userRepo.save(user);
    }

    public User transferFunds(String senderAccountNumber, String receiverAccountNumber, Double amount) {
        User sender = getUserByAccountNumber(senderAccountNumber);
        User receiver = getUserByAccountNumber(receiverAccountNumber);

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        double newSenderBalance = sender.getBalance() - amount;

        if (newSenderBalance < 500) {
            throw new IllegalArgumentException("Transfer failed: Sender's account will fall below the minimum balance of ₹500.");
        }

        sender.setBalance(newSenderBalance);
        receiver.setBalance(receiver.getBalance() + amount);

        userRepo.save(receiver);
        transactionRepo.save(new Transaction(senderAccountNumber, "TRANSFER_OUT", amount));
        transactionRepo.save(new Transaction(receiverAccountNumber, "TRANSFER_IN", amount));

        return userRepo.save(sender);
    }

    public Double checkBalance(String accountNumber) {
        return getUserByAccountNumber(accountNumber).getBalance();
    }

    public User getUserByAccountNumber(String accountNumber) {
        return userRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new UserNotFoundException("User with account number " + accountNumber + " not found"));
    }


    public User createUser(User user) {
        if (user.getBalance() < 500) {
            throw new IllegalArgumentException("Opening balance must be at least ₹500");
        }
        return userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
    public List<Transaction> getAllTransactions(String accountNumber) {
        return transactionRepo.findByAccountNumberOrderByTimestampDesc(accountNumber);
    }



    @SpringBootApplication
    public static class AtmMachineApplication {
        public static void main(String[] args) {
            SpringApplication.run(AtmMachineApplication.class, args);
        }
    }
}
