package org.shakaal.collegemanagementapp.dao;

import org.shakaal.collegemanagementapp.database.DatabaseClass;
import org.shakaal.collegemanagementapp.models.ExpenseCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseCategoryDAO {


    // ================= GET ALL EXPENSE CATEGORIES =================

    public List<ExpenseCategory> getAllExpenseCategories() {

        List<ExpenseCategory> categories = new ArrayList<>();

        String sql = """
                SELECT *
                FROM expense_categories
                ORDER BY category_name
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                ExpenseCategory category = new ExpenseCategory();

                category.setCategoryId(resultSet.getInt("category_id"));

                category.setCategoryName(resultSet.getString("category_name"));

                categories.add(category);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return categories;
    }


    // ================= GET BY ID =================

    public ExpenseCategory getExpenseCategoryById(int categoryId) {

        String sql = """
                SELECT *
                FROM expense_categories
                WHERE category_id = ?
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, categoryId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    ExpenseCategory category = new ExpenseCategory();

                    category.setCategoryId(resultSet.getInt("category_id"));

                    category.setCategoryName(resultSet.getString("category_name"));

                    return category;
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }


    // ================= ADD =================

    public boolean addExpenseCategory(ExpenseCategory category) {

        String sql = """
                INSERT INTO expense_categories (category_name)
                VALUES (?)
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, category.getCategoryName());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // ================= UPDATE =================

    public boolean updateExpenseCategory(ExpenseCategory category) {

        String sql = """
                UPDATE expense_categories
                SET category_name = ?
                WHERE category_id = ?
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, category.getCategoryName());
            statement.setInt(2, category.getCategoryId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // ================= DELETE =================

    public boolean deleteExpenseCategory(int categoryId) {

        String sql = """
                DELETE FROM expense_categories
                WHERE category_id = ?
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, categoryId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }
}