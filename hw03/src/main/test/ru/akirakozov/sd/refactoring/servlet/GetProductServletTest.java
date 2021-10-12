package ru.akirakozov.sd.refactoring.servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.database.DB;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetProductServletTest {
    private static final int PORT = 8082;
    private static final String SERVLET_PATH = "/get-products";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Server server = new Server(PORT);

    @BeforeAll
    public static void initServer() {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new GetProductsServlet()), SERVLET_PATH);
    }

    @BeforeEach
    public void initDB() throws Exception {
        DB.init();
        server.start();
    }

    @AfterEach
    public void dropDB () throws Exception {
        DB.drop();
        server.stop();
    }

    @Test
    public void sampleGetProduct() throws Exception {
        StringBuilder result = new StringBuilder();
        result.append("<html><body>\r\n");

        DB.update("apple", 100L);
        result.append("apple\t100</br>\r\n");

        DB.update("pineapple", 1000L);
        result.append("pineapple\t1000</br>\r\n");

        result.append("</body></html>\r\n");
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals( result.toString(), response.body());
    }

    @Test
    public void emptyGetProduct() throws Exception {
        StringBuilder result = new StringBuilder();
        result.append("<html><body>\r\n");
        result.append("</body></html>\r\n");
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals( result.toString(), response.body());
    }
}