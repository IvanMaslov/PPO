package ru.akirakozov.sd.refactoring.database;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Yes, that's DB singleton
 * When I worked in Yandex we have something like that
 * It might become a problem, but only after five years of growth
 */
public class DB {
    private static final String DB_NAME = "jdbc:sqlite:test.db";

    public static void init() throws SQLException {
        try (Connection c = DriverManager.getConnection(DB_NAME)) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    public static void drop() throws SQLException {
        try (Connection c = DriverManager.getConnection(DB_NAME)) {
            String sql = "DROP TABLE PRODUCT";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    public static void update(String name, Long price) {
        update(new Product(name, price));
    }

    public static void update(Product product) {
        try {
            try (Connection c = DriverManager.getConnection(DB_NAME)) {
                String sql = "INSERT INTO PRODUCT " +
                        "(NAME, PRICE) VALUES " + product.toSqlValue();
                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Product> getValues(String sqlRequest) {
        try {
            List<Product> result = new ArrayList<>();
            try (Connection c = DriverManager.getConnection(DB_NAME)) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(sqlRequest);

                while (rs.next()) {
                    String name = rs.getString("name");
                    long price = rs.getInt("price");
                    result.add(new Product(name, price));
                }

                rs.close();
                stmt.close();
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static long getScalar(String sqlRequest) {
        try {
            long result = 0L;
            try (Connection c = DriverManager.getConnection(DB_NAME)) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(sqlRequest);

                if (rs.next()) {
                    result = rs.getInt(1);
                }

                rs.close();
                stmt.close();
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
