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

    public User creditMoney(String cardNumber, Double amount) {
        User user = getUserByCardNumber(cardNumber);
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        user.setInitialDeposit(user.getInitialDeposit() + amount);
        transactionRepo.save(new Transaction(cardNumber, "CREDIT", amount));
        return userRepo.save(user);
    }
    public String withdrawMoney(String cardNumber, Double amount, String accountType) {
        User user = getUserByCardNumber(cardNumber);

        if (!user.getAccountType().equalsIgnoreCase(accountType)) {
            throw new IllegalArgumentException("Invalid account type for this user.");
        }

        double minBalance;
        if ("Savings".equalsIgnoreCase(accountType)) {
            minBalance = 500;
        } else if ("Current".equalsIgnoreCase(accountType)) {
            minBalance = 1000;
        } else {
            throw new IllegalArgumentException("Unsupported account type.");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }

        double remainingBalance = user.getInitialDeposit() - amount;

        if (remainingBalance < minBalance) {
            throw new IllegalArgumentException("Insufficient balance: minimum balance requirement not met.");
        }

        user.setInitialDeposit(remainingBalance);
        userRepo.save(user);
        transactionRepo.save(new Transaction(cardNumber, "DEBIT", amount));

        return "Money withdrawn successfully.";
    }

    public User changePin(String cardNumber, String newPin) {
        User user = getUserByCardNumber(cardNumber);
        user.setPin(newPin);
        return userRepo.save(user);
    }

    public User transferFunds(String senderCardNumber, String receiverCardNumber, Double amount) {
        User sender = getUserByCardNumber(senderCardNumber);
        User receiver = getUserByCardNumber(receiverCardNumber);

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        double newSenderBalance = sender.getInitialDeposit() - amount;

        if (newSenderBalance < 500) {
            throw new IllegalArgumentException("Transfer failed: Sender's account will fall below the minimum balance of ₹500.");
        }

        sender.setInitialDeposit(newSenderBalance);
        receiver.setInitialDeposit(receiver.getInitialDeposit() + amount);

        userRepo.save(receiver);
        transactionRepo.save(new Transaction(senderCardNumber, "TRANSFER_OUT", amount));
        transactionRepo.save(new Transaction(receiverCardNumber, "TRANSFER_IN", amount));

        return userRepo.save(sender);
    }

    public Double checkBalance(String cardNumber) {
        return getUserByCardNumber(cardNumber).getInitialDeposit();
    }

    public User getUserByCardNumber(String cardNumber) {
        return userRepo.findByCardNumber(cardNumber)
                .orElseThrow(() -> new UserNotFoundException("User with account number " + cardNumber + " not found"));
    }

    public User createUser(User user) {
        if (user.getAccountType() == null) {
            throw new IllegalArgumentException("Account type is required.");
        }

        String type = user.getAccountType().trim().toLowerCase();
        double initialDeposit = user.getInitialDeposit(); // Assuming balance field is used for deposit

        switch (type) {
            case "savings":
                if (initialDeposit < 500) {
                    throw new IllegalArgumentException("Minimum initial deposit for a Savings account is ₹500.");
                }
                break;
            case "current":
                if (initialDeposit < 1000) {
                    throw new IllegalArgumentException("Minimum initial deposit for a Current account is ₹1000.");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid account type. Must be 'Savings' or 'Current'.");
        }

        return userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
    public List<Transaction> getAllTransactions(String cardNumber) {
        return transactionRepo.findByCardNumberOrderByTimestampDesc(cardNumber);
    }



    @SpringBootApplication
    public static class AtmMachineApplication {
        public static void main(String[] args) {
            SpringApplication.run(AtmMachineApplication.class, args);
        }
    }
}
