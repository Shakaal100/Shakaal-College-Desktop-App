package org.shakaal.collegemanagementapp.dao;

import org.shakaal.collegemanagementapp.database.DatabaseClass;
import org.shakaal.collegemanagementapp.models.TeacherSalaryPayment;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class TeacherSalaryPaymentDAO {


    // ================= ADD PAYMENT =================

    public boolean addTeacherSalaryPayment(
            TeacherSalaryPayment payment) {

        String sql = """
                INSERT INTO teacher_salary_payments (
                    teacher_id,
                    payment_month,
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

            statement.setInt(1, payment.getTeacherId());

            statement.setString(
                    2,
                    payment.getPaymentMonth().toString()
            );

            statement.setDouble(3, payment.getAmount());

            statement.setString(
                    4,
                    payment.getPaymentDate().toString()
            );

            statement.setInt(
                    5,
                    payment.getPaymentMethodId()
            );

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

    public List<TeacherSalaryPayment> getAllTeacherSalaryPayments() {

        List<TeacherSalaryPayment> payments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM teacher_salary_payments
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

    public TeacherSalaryPayment getPaymentById(int paymentId) {

        String sql = """
                SELECT *
                FROM teacher_salary_payments
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

    public boolean hasTeacherBeenPaidForMonth(
            int teacherId,
            YearMonth paymentMonth) {

        String sql = """
                SELECT 1
                FROM teacher_salary_payments
                WHERE teacher_id = ?
                  AND payment_month = ?
                LIMIT 1
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, teacherId);

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

    public List<TeacherSalaryPayment> getPaymentsForMonth(
            YearMonth paymentMonth) {

        List<TeacherSalaryPayment> payments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM teacher_salary_payments
                WHERE payment_month = ?
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
                FROM teacher_salary_payments
                WHERE payment_month = ?
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

    public List<TeacherSalaryPayment> searchPayments(
            String keyword) {

        List<TeacherSalaryPayment> payments = new ArrayList<>();

        String sql = """
                SELECT teacher_salary_payments.*
                FROM teacher_salary_payments
                JOIN teachers
                    ON teacher_salary_payments.teacher_id =
                       teachers.teacher_id
                WHERE
                    teacher_salary_payments.payment_month LIKE ?
                    OR teacher_salary_payments.reference_number LIKE ?
                    OR teachers.full_name LIKE ?
                    OR teachers.phone LIKE ?
                    OR teachers.email LIKE ?
                    OR teachers.specialization LIKE ?
                ORDER BY teacher_salary_payments.payment_date DESC
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

    private TeacherSalaryPayment mapPayment(
            ResultSet resultSet) throws SQLException {

        TeacherSalaryPayment payment = new TeacherSalaryPayment();

        payment.setPaymentId(
                resultSet.getInt("payment_id")
        );

        payment.setTeacherId(
                resultSet.getInt("teacher_id")
        );

        payment.setPaymentMonth(
                YearMonth.parse(
                        resultSet.getString("payment_month")
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
