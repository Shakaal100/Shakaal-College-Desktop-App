package org.shakaal.collegemanagementapp.models;

import java.time.LocalDate;
import java.time.YearMonth;

public class TeacherSalaryPayment {

    private int paymentId;
    private int teacherId;
    private YearMonth paymentMonth;
    private double amount;
    private LocalDate paymentDate;
    private int paymentMethodId;
    private String referenceNumber;
    private String notes;


    public TeacherSalaryPayment() {
    }


    public TeacherSalaryPayment(int teacherId, YearMonth paymentMonth, double amount, LocalDate paymentDate, int paymentMethodId, String referenceNumber, String notes) {

        this.teacherId = teacherId;
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


    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
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