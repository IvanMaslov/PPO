package ru.akirakozov.sd.refactoring.database;

import java.io.PrintWriter;
import java.util.Optional;

public class HtmlCreator {

    private enum APPLY_CASE {
        SAMPLING,
        SCALAR
    }

    private static void apply(PrintWriter writer,
                              String sqlRequest,
                              Optional<String> message,
                              APPLY_CASE acase) {
        writer.println("<html><body>");
        message.ifPresent(writer::println);

        switch (acase) {
            case SAMPLING:
                for (Product product : DB.getValues(sqlRequest))
                    product.write(writer);
                break;
            case SCALAR:
                writer.println(DB.getScalar(sqlRequest));
                break;
        }
        writer.println("</body></html>");
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
