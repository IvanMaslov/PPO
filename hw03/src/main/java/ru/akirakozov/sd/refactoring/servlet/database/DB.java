package ru.akirakozov.sd.refactoring.servlet.database;

import java.io.PrintWriter;
import java.sql.*;
import java.util.Optional;
import java.util.function.BiFunction;

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
        try {
            try (Connection c = DriverManager.getConnection(DB_NAME)) {
                String sql = "INSERT INTO PRODUCT " +
                        "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private enum APPLY_CASE {
        SAMPLING,
        SCALAR
    }

    private static void apply(PrintWriter writer,
                              String sqlRequest,
                              Optional<String> message,
                              APPLY_CASE acase) {
        try {
            try (Connection c = DriverManager.getConnection(DB_NAME)) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(sqlRequest);
                writer.println("<html><body>");
                message.ifPresent(writer::println);

                switch (acase) {
                    case SAMPLING:
                        while (rs.next()) {
                            String name = rs.getString("name");
                            int price = rs.getInt("price");
                            writer.println(name + "\t" + price + "</br>");
                        }
                        break;
                    case SCALAR:
                        if (rs.next()) {
                            writer.println(rs.getInt(1));
                        }
                        break;
                }
                writer.println("</body></html>");

                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void readAsHtml(PrintWriter writer) {
        apply(writer, "SELECT * FROM PRODUCT", Optional.empty(), APPLY_CASE.SAMPLING);
    }

    public static void applyWithSample(PrintWriter writer, String sqlRequest, String message) {
        apply(writer, sqlRequest, Optional.of(message), APPLY_CASE.SAMPLING);
    }

    public static void applyNoSample(PrintWriter writer, String sqlRequest, String message) {
        apply(writer, sqlRequest, Optional.of(message), APPLY_CASE.SCALAR);
    }
}
