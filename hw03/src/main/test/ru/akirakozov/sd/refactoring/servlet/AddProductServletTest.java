package ru.akirakozov.sd.refactoring.servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.*;
import ru.akirakozov.sd.refactoring.database.DB;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class AddProductServletTest {
    private static final int PORT = 8082;
    private static final String SERVLET_PATH = "/add-product";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Server server = new Server(PORT);

    @BeforeAll
    public static void initServer() {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new AddProductServlet()), SERVLET_PATH);
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
    public void sampleAddProduct() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH + "?name=phone&price=100"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void noPriceAddProduct() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH + "?name=phone"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(500, response.statusCode());
    }

    @Test
    public void priceAsStringAddProduct() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH + "?name=phone&price=price"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(500, response.statusCode());
    }

    @Test
    public void noNameAddProduct() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH + "?price=100"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void emptyRequestAddProduct() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + PORT + SERVLET_PATH + "?"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(500, response.statusCode());
    }
}