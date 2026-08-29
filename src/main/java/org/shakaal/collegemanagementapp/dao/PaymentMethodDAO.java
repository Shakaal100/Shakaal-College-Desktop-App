package org.shakaal.collegemanagementapp.dao;

import org.shakaal.collegemanagementapp.models.PaymentMethod;
import org.shakaal.collegemanagementapp.database.DatabaseClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDAO {


    // ================= GET ALL PAYMENT METHODS =================

    public List<PaymentMethod> getAllPaymentMethods() {

        List<PaymentMethod> paymentMethods = new ArrayList<>();

        String sql = """
                SELECT *
                FROM payment_methods
                ORDER BY method_name
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                PaymentMethod paymentMethod = new PaymentMethod();

                paymentMethod.setPaymentMethodId(resultSet.getInt("payment_method_id"));

                paymentMethod.setMethodName(resultSet.getString("method_name"));

                paymentMethods.add(paymentMethod);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return paymentMethods;
    }


    // ================= GET BY ID =================

    public PaymentMethod getPaymentMethodById(int paymentMethodId) {

        String sql = """
                SELECT *
                FROM payment_methods
                WHERE payment_method_id = ?
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, paymentMethodId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    PaymentMethod paymentMethod = new PaymentMethod();

                    paymentMethod.setPaymentMethodId(resultSet.getInt("payment_method_id"));

                    paymentMethod.setMethodName(resultSet.getString("method_name"));

                    return paymentMethod;
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }


    // ================= ADD =================

    public boolean addPaymentMethod(PaymentMethod paymentMethod) {

        String sql = """
                INSERT INTO payment_methods (method_name)
                VALUES (?)
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, paymentMethod.getMethodName());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // ================= UPDATE =================

    public boolean updatePaymentMethod(PaymentMethod paymentMethod) {

        String sql = """
                UPDATE payment_methods
                SET method_name = ?
                WHERE payment_method_id = ?
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, paymentMethod.getMethodName());
            statement.setInt(2, paymentMethod.getPaymentMethodId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // ================= DELETE =================

    public boolean deletePaymentMethod(int paymentMethodId) {

        String sql = """
                DELETE FROM payment_methods
                WHERE payment_method_id = ?
                """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, paymentMethodId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }
}
