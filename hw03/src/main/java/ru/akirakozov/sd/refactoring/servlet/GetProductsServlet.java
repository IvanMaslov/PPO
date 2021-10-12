package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.HtmlCreator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends AbstractServlet {

    @Override
    protected void servletLogic(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HtmlCreator.readAsHtml(response.getWriter());
    }
}
