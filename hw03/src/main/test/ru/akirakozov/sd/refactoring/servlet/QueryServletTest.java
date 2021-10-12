package ru.akirakozov.sd.refactoring.servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.servlet.database.DB;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryServletTest {
    private static final int PORT = 8082;
    private static final String SERVLET_PATH = "/query";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Server server = new Server(PORT);

    @BeforeAll
    public static void initServer() {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new QueryServlet()), SERVLET_PATH);
    }

    @BeforeEach
    public void initDB() throws Exception {
        DB.init();
        storeDB();
        server.start();
    }

    public void storeDB() {
        DB.update("A", 10L);
        DB.update("B", 11L);
        DB.update("C", 12L);
        DB.update("D", 13L);
        DB.update("E", 14L);
    }

    @AfterEach
    public void dropDB () throws Exception {
        DB.drop();
        server.stop();
    }

    @Test
    public void maxQuery() throws Exception {
        StringBuilder result = new StringBuilder();
        result.append("<html><body>\r\n");
        result.append("<h1>Product with max price: </h1>\r\n");
        result.append("E\t14</br>\r\n");
        result.append("</body></html>\r\n");
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH + "?command=max"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(result.toString(), response.body());
    }

    @Test
    public void minQuery() throws Exception {
        StringBuilder result = new StringBuilder();
        result.append("<html><body>\r\n");
        result.append("<h1>Product with min price: </h1>\r\n");
        result.append("A\t10</br>\r\n");
        result.append("</body></html>\r\n");
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH + "?command=min"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(result.toString(), response.body());
    }

    @Test
    public void sumQuery() throws Exception {
        StringBuilder result = new StringBuilder();
        result.append("<html><body>\r\n");
        result.append("Summary price: \r\n");
        result.append("60\r\n");
        result.append("</body></html>\r\n");
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH + "?command=sum"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(result.toString(), response.body());
    }

    @Test
    public void countQuery() throws Exception {
        StringBuilder result = new StringBuilder();
        result.append("<html><body>\r\n");
        result.append("Number of products: \r\n");
        result.append("5\r\n");
        result.append("</body></html>\r\n");
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH + "?command=count"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(result.toString(), response.body());
    }

    @Test
    public void undefinedQuery() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH + "?command=bullshit"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("Unknown command: bullshit\r\n", response.body());
    }

    @Test
    public void emptyQuery() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH + "?command="))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("Unknown command: \r\n", response.body());
    }
}