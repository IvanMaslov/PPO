package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.HtmlCreator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractServlet {
    @Override
    protected void servletLogic(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            String sqlRequest = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
            String message = "<h1>Product with max price: </h1>";
            HtmlCreator.applyWithSample(response.getWriter(), sqlRequest, message);
        } else if ("min".equals(command)) {
            String sqlRequest = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
            String message = "<h1>Product with min price: </h1>";
            HtmlCreator.applyWithSample(response.getWriter(), sqlRequest, message);
        } else if ("sum".equals(command)) {
            String sqlRequest = "SELECT SUM(price) FROM PRODUCT";
            String message = "Summary price: ";
            HtmlCreator.applyNoSample(response.getWriter(), sqlRequest, message);
        } else if ("count".equals(command)) {
            String sqlRequest = "SELECT COUNT(*) FROM PRODUCT";
            String message = "Number of products: ";
            HtmlCreator.applyNoSample(response.getWriter(), sqlRequest, message);
        } else {
            response.getWriter().println("Unknown command: " + command);
        }
    }

}
