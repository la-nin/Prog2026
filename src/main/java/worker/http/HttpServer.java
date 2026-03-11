package worker.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServer {
    private final int port;
    private final HttpRequestParser parser;
    private final WorkerRouter router;
    private final HttpResponseWriter responseWriter;

    public HttpServer(int port, HttpRequestParser parser, WorkerRouter router, HttpResponseWriter responseWriter) {
        this.port = port;
        this.parser = parser;
        this.router = router;
        this.responseWriter = responseWriter;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Connected to " + port);
            while (true) {
                Socket client = serverSocket.accept();
                handle(client);
            }
        }
    }

    private void handle(Socket client) {
        try (Socket socket = client;
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            HttpResponse response;
            try {
                HttpRequest request = parser.parse(reader);
                response = router.route(request);
            } catch (IllegalArgumentException ex) {
                response = HttpResponse.badRequest();
            }

            responseWriter.write(socket.getOutputStream(), response);
        } catch (IOException ioException) {
            System.err.println("Failed connection: " + ioException.getMessage());
        }
    }
}
