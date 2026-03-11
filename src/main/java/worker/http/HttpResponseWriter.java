package worker.http;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpResponseWriter {

    public void write(OutputStream outputStream, HttpResponse response) throws IOException {
        StringBuilder head = new StringBuilder();
        head.append("HTTP/1.1 ")
                .append(response.statusCode())
                .append(' ')
                .append(response.reason())
                .append("\r\n");

        response.headers().forEach((name, value) -> head.append(name).append(": ").append(value).append("\r\n"));
        head.append("\r\n");

        outputStream.write(head.toString().getBytes(StandardCharsets.UTF_8));
        outputStream.write(response.body());
        outputStream.flush();
    }
}
