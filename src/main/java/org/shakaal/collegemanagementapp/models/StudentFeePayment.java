package org.shakaal.collegemanagementapp.models;

import java.time.LocalDate;
import java.time.YearMonth;

public class StudentFeePayment {

    private int paymentId;

    private int studentId;

    private YearMonth paymentMonth;

    private double amount;

    private LocalDate paymentDate;

    private int paymentMethodId;

    private String referenceNumber;

    private String notes;


    public StudentFeePayment() {
    }


    public StudentFeePayment(int paymentId, int studentId, YearMonth paymentMonth, double amount, LocalDate paymentDate, int paymentMethodId, String referenceNumber, String notes) {

        this.paymentId = paymentId;

        this.studentId = studentId;

        this.paymentMonth = paymentMonth;

        this.amount = amount;

        this.paymentDate = paymentDate;

        this.paymentMethodId = paymentMethodId;

        this.referenceNumber = referenceNumber;

        this.notes = notes;
    }


    public int getPaymentId() {

        return paymentId;
    }


    public void setPaymentId(int paymentId) {

        this.paymentId = paymentId;
    }


    public int getStudentId() {

        return studentId;
    }


    public void setStudentId(int studentId) {

        this.studentId = studentId;
    }


    public YearMonth getPaymentMonth() {

        return paymentMonth;
    }


    public void setPaymentMonth(YearMonth paymentMonth) {

        this.paymentMonth = paymentMonth;
    }


    public double getAmount() {

        return amount;
    }


    public void setAmount(double amount) {

        this.amount = amount;
    }


    public LocalDate getPaymentDate() {

        return paymentDate;
    }


    public void setPaymentDate(LocalDate paymentDate) {

        this.paymentDate = paymentDate;
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


    public String getNotes() {

        return notes;
    }


    public void setNotes(String notes) {

        this.notes = notes;
    }
}