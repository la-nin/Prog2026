package worker.http;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
    private final int statusCode;
    private final String reason;
    private final byte[] body;
    private final Map<String, String> headers;

    private HttpResponse(int statusCode, String reason, String contentType, String body) {
        this.statusCode = statusCode;
        this.reason = reason;
        this.body = body.getBytes(StandardCharsets.UTF_8);
        this.headers = new LinkedHashMap<>();
        headers.put("Content-Type", contentType);
        headers.put("Content-Length", String.valueOf(this.body.length));
        headers.put("Connection", "close");
    }

    public static HttpResponse ok(String body) {
        return new HttpResponse(200, "OK", "text/plain; charset=utf-8", body);
    }

    public static HttpResponse notFound() {
        return new HttpResponse(404, "Not Found", "text/plain; charset=utf-8", "Not Found");
    }

    public static HttpResponse methodNotAllowed() {
        return new HttpResponse(405, "Method Not Allowed", "text/plain; charset=utf-8", "Method Not Allowed");
    }

    public static HttpResponse badRequest() {
        return new HttpResponse(400, "Bad Request", "text/plain; charset=utf-8", "Bad Request");
    }

    public int statusCode() {
        return statusCode;
    }

    public String reason() {
        return reason;
    }

    public byte[] body() {
        return body;
    }

    public Map<String, String> headers() {
        return headers;
    }
}

