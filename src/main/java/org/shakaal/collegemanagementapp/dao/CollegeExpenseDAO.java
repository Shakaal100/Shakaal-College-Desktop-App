package org.shakaal.collegemanagementapp.dao;

import org.shakaal.collegemanagementapp.database.DatabaseClass;
import org.shakaal.collegemanagementapp.models.CollegeExpense;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class CollegeExpenseDAO {


    // ================= ADD EXPENSE =================

    public boolean addExpense(CollegeExpense expense) {

        String sql = """
                INSERT INTO expenses (
                    category_id,
                    amount,
                    expense_date,
                    payment_method_id,
                    reference_number,
                    description
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, expense.getCategoryId());

            statement.setDouble(2, expense.getAmount());

            statement.setString(
                    3,
                    expense.getExpenseDate().toString()
            );

            statement.setInt(
                    4,
                    expense.getPaymentMethodId()
            );

            statement.setString(
                    5,
                    expense.getReferenceNumber()
            );

            statement.setString(
                    6,
                    expense.getDescription()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // ================= GET ALL EXPENSES =================

    public List<CollegeExpense> getAllExpenses() {

        List<CollegeExpense> expenses = new ArrayList<>();

        String sql = """
                SELECT *
                FROM expenses
                ORDER BY expense_date DESC, expense_id DESC
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                expenses.add(mapExpense(resultSet));
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return expenses;
    }


    // ================= GET EXPENSE BY ID =================

    public CollegeExpense getExpenseById(int expenseId) {

        String sql = """
                SELECT *
                FROM expenses
                WHERE expense_id = ?
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, expenseId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return mapExpense(resultSet);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }


    // ================= GET EXPENSES FOR MONTH =================

    public List<CollegeExpense> getExpensesForMonth(
            YearMonth month) {

        List<CollegeExpense> expenses = new ArrayList<>();

        String sql = """
                SELECT *
                FROM expenses
                WHERE expense_date >= ?
                  AND expense_date < ?
                ORDER BY expense_date DESC, expense_id DESC
                """;

        LocalDate startDate = month.atDay(1);
        LocalDate nextMonth = month.plusMonths(1).atDay(1);

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, startDate.toString());
            statement.setString(2, nextMonth.toString());

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    expenses.add(mapExpense(resultSet));
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return expenses;
    }


    // ================= GET TOTAL FOR MONTH =================

    public double getTotalForMonth(YearMonth month) {

        String sql = """
                SELECT COALESCE(SUM(amount), 0)
                FROM expenses
                WHERE expense_date >= ?
                  AND expense_date < ?
                """;

        LocalDate startDate = month.atDay(1);
        LocalDate nextMonth = month.plusMonths(1).atDay(1);

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, startDate.toString());
            statement.setString(2, nextMonth.toString());

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


    // ================= SEARCH EXPENSES =================

    public List<CollegeExpense> searchExpenses(String keyword) {

        List<CollegeExpense> expenses = new ArrayList<>();

        String sql = """
                SELECT expenses.*
                FROM expenses
                JOIN expense_categories
                    ON expenses.category_id =
                       expense_categories.category_id
                WHERE
                    expenses.reference_number LIKE ?
                    OR expenses.description LIKE ?
                    OR expense_categories.category_name LIKE ?
                ORDER BY expenses.expense_date DESC
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            String searchKeyword = "%" + keyword + "%";

            statement.setString(1, searchKeyword);
            statement.setString(2, searchKeyword);
            statement.setString(3, searchKeyword);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    expenses.add(mapExpense(resultSet));
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return expenses;
    }


    // ================= MAP RESULT SET =================

    private CollegeExpense mapExpense(
            ResultSet resultSet) throws SQLException {

        CollegeExpense expense = new CollegeExpense();

        expense.setExpenseId(
                resultSet.getInt("expense_id")
        );

        expense.setCategoryId(
                resultSet.getInt("category_id")
        );

        expense.setAmount(
                resultSet.getDouble("amount")
        );

        expense.setExpenseDate(
                LocalDate.parse(
                        resultSet.getString("expense_date")
                )
        );

        expense.setPaymentMethodId(
                resultSet.getInt("payment_method_id")
        );

        expense.setReferenceNumber(
                resultSet.getString("reference_number")
        );

        expense.setDescription(
                resultSet.getString("description")
        );

        return expense;
    }
}