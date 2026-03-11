package worker.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

    public HttpRequest parse(BufferedReader reader) throws IOException {
        String request = reader.readLine();
        if (request == null || request.isEmpty()) {
            throw new IllegalArgumentException("Missing request line");
        }

        String[] parsed = request.split("\\s+");
        if (parsed.length < 2) {
            throw new IllegalArgumentException("Incorrect request line");
        }

        String method = parsed[0];
        String path = normalizePath(parsed[1]);
        String version = parsed[2];

        if (!"HTTP/1.1".equals(version)) {
            throw new IllegalArgumentException("Unsupported HTTP version");
        }

        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            //headers
            int sepIndex = line.indexOf(':');
            if (sepIndex <= 0) {
                throw new IllegalArgumentException("Malformed header line");
            }
            String name = line.substring(0, sepIndex).trim();
            String value = line.substring(sepIndex + 1).trim();
            headers.put(name, value);
        }

        if (!headers.containsKey("Host")) {
            throw new IllegalArgumentException("HTTP/1.1 requires Host header");
        }

        return new HttpRequest(method, path, version, headers);
    }

    private String normalizePath(String rawPath) {
        if (rawPath == null || rawPath.isBlank() || rawPath.charAt(0) != '/') {
            throw new IllegalArgumentException("Invalid request target");
        }

        int queryIdx = rawPath.indexOf('?');
        return queryIdx >= 0 ? rawPath.substring(0, queryIdx) : rawPath;
    }
}

