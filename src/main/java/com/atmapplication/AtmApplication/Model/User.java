package com.atmapplication.AtmApplication.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Card number is required")
    @Column(name = "card_number", unique = true, nullable = false)
    @Size(min = 16, max = 16 , message = "Card Number must be 16 digits")
    private String cardNumber;

    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "PIN is required")
    @Size(min = 4, max = 4, message = "PIN must be 4 digits")
    private String pin;

    @Column(unique = true)
    @Email(message = "Email should be valid")
    @Email(message = "Invalid email format")  // Built-in Jakarta/JavaX validation (simplest option)
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "Invalid email format")
    private String email;

    @Column(name= "account_type")
    private String accountType;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String contact;


    @Column(name = "date_of_birth")  // Good practice to use snake_case for column names
    private String dob;  // Consider using LocalDate if you want to handle it as a date type

    @PositiveOrZero(message = "Balance cannot be negative")
    private Double initialDeposit;

    // 🔽 Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }


    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Double getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(Double initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
