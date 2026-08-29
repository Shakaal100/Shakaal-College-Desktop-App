package org.shakaal.collegemanagementapp.models;

import java.time.LocalDate;

public class CollegeExpense {

    private int expenseId;
    private int categoryId;
    private double amount;
    private LocalDate expenseDate;
    private int paymentMethodId;
    private String referenceNumber;
    private String description;


    public CollegeExpense() {
    }


    public CollegeExpense(int categoryId, double amount, LocalDate expenseDate, int paymentMethodId, String referenceNumber, String description) {

        this.categoryId = categoryId;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.paymentMethodId = paymentMethodId;
        this.referenceNumber = referenceNumber;
        this.description = description;
    }


    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }


    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }


    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
