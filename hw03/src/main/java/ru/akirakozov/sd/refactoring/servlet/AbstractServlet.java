package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public abstract class AbstractServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        servletLogic(request, response);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    abstract void servletLogic(HttpServletRequest request, HttpServletResponse response)
            throws IOException;
}
