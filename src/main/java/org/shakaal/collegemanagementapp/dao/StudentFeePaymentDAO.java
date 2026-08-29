package org.shakaal.collegemanagementapp.dao;

import org.shakaal.collegemanagementapp.database.DatabaseClass;
import org.shakaal.collegemanagementapp.models.StudentFeePayment;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class StudentFeePaymentDAO {


    // ================= ADD PAYMENT =================

    public boolean addStudentFeePayment(StudentFeePayment payment) {

        String sql = """
                INSERT INTO student_fee_payments (
                    student_id,
                    payment_period,
                    amount,
                    payment_date,
                    payment_method_id,
                    reference_number,
                    notes
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, payment.getStudentId());

            statement.setString(
                    2,
                    payment.getPaymentMonth().toString()
            );

            statement.setDouble(3, payment.getAmount());

            statement.setString(
                    4,
                    payment.getPaymentDate().toString()
            );

            statement.setInt(5, payment.getPaymentMethodId());

            statement.setString(
                    6,
                    payment.getReferenceNumber()
            );

            statement.setString(
                    7,
                    payment.getNotes()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // ================= GET ALL PAYMENTS =================

    public List<StudentFeePayment> getAllStudentFeePayments() {

        List<StudentFeePayment> payments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM student_fee_payments
                ORDER BY payment_date DESC, payment_id DESC
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                payments.add(mapPayment(resultSet));
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return payments;
    }


    // ================= GET PAYMENT BY ID =================

    public StudentFeePayment getPaymentById(int paymentId) {

        String sql = """
                SELECT *
                FROM student_fee_payments
                WHERE payment_id = ?
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, paymentId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return mapPayment(resultSet);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }


    // ================= CHECK PAYMENT FOR MONTH =================

    public boolean hasStudentPaidForMonth(
            int studentId,
            YearMonth paymentMonth
    ) {

        String sql = """
                SELECT 1
                FROM student_fee_payments
                WHERE student_id = ?
                  AND payment_period = ?
                LIMIT 1
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, studentId);

            statement.setString(
                    2,
                    paymentMonth.toString()
            );

            try (ResultSet resultSet = statement.executeQuery()) {

                return resultSet.next();
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // ================= GET PAYMENTS FOR MONTH =================

    public List<StudentFeePayment> getPaymentsForMonth(
            YearMonth paymentMonth
    ) {

        List<StudentFeePayment> payments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM student_fee_payments
                WHERE payment_period = ?
                ORDER BY payment_date DESC, payment_id DESC
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    paymentMonth.toString()
            );

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    payments.add(mapPayment(resultSet));
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return payments;
    }


    // ================= GET TOTAL FOR MONTH =================

    public double getTotalForMonth(YearMonth paymentMonth) {

        String sql = """
                SELECT COALESCE(SUM(amount), 0)
                FROM student_fee_payments
                WHERE payment_period = ?
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    paymentMonth.toString()
            );

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return resultSet.getDouble(1);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return 0;
    }


    // ================= SEARCH PAYMENTS =================

    public List<StudentFeePayment> searchPayments(String keyword) {

        List<StudentFeePayment> payments = new ArrayList<>();

        String sql = """
                SELECT student_fee_payments.*
                FROM student_fee_payments
                JOIN students
                    ON student_fee_payments.student_id = students.student_id
                WHERE
                    student_fee_payments.payment_period LIKE ?
                    OR student_fee_payments.reference_number LIKE ?
                    OR students.first_name LIKE ?
                    OR students.last_name LIKE ?
                    OR students.phone LIKE ?
                    OR students.email LIKE ?
                ORDER BY student_fee_payments.payment_date DESC
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            String searchKeyword = "%" + keyword + "%";

            for (int i = 1; i <= 6; i++) {

                statement.setString(i, searchKeyword);
            }

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    payments.add(mapPayment(resultSet));
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return payments;
    }


    // ================= MAP RESULT SET =================

    private StudentFeePayment mapPayment(ResultSet resultSet)
            throws SQLException {

        StudentFeePayment payment = new StudentFeePayment();

        payment.setPaymentId(
                resultSet.getInt("payment_id")
        );

        payment.setStudentId(
                resultSet.getInt("student_id")
        );

        payment.setPaymentMonth(
                YearMonth.parse(
                        resultSet.getString("payment_period")
                )
        );

        payment.setAmount(
                resultSet.getDouble("amount")
        );

        payment.setPaymentDate(
                LocalDate.parse(
                        resultSet.getString("payment_date")
                )
        );

        payment.setPaymentMethodId(
                resultSet.getInt("payment_method_id")
        );

        payment.setReferenceNumber(
                resultSet.getString("reference_number")
        );

        payment.setNotes(
                resultSet.getString("notes")
        );

        return payment;
    }
}