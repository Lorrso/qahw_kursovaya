package ru.netology.shop.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;

public class SQLHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/student", "student", "logviewer");
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        var connection = getConn();
        String query = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        String statusPayment = QUERY_RUNNER.query(connection, query, new ScalarHandler<>());
        return statusPayment;
    }

    @SneakyThrows
    public static boolean getOrderStatus() {
        var connection = getConn();
        String query = "SELECT payment_id FROM order_entity ORDER BY created DESC LIMIT 1";
        String orderId = QUERY_RUNNER.query(connection, query, new ScalarHandler<>());
        boolean statusOrder = false;
        if (orderId != null) {
            statusOrder = true;
        }
        return statusOrder;
    }

    @SneakyThrows
    public static void cleanDatabase() {
        var connection = getConn();
        QUERY_RUNNER.execute(connection, "DELETE FROM credit_request_entity");
        QUERY_RUNNER.execute(connection, "DELETE FROM order_entity");
        QUERY_RUNNER.execute(connection, "DELETE FROM payment_entity");
    }
}