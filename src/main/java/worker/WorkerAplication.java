package worker;

import worker.http.HttpRequestParser;
import worker.http.HttpResponseWriter;
import worker.http.HttpServer;
import worker.http.WorkerRouter;

public class WorkerAplication {
    public static void main(String[] args) throws Exception {
        int port = resolvePort(args);
        HttpServer server = new HttpServer(port, new HttpRequestParser(), new WorkerRouter(), new HttpResponseWriter());
        server.start();
    }

    private static int resolvePort(String[] args) {
        if (args.length > 0) {
            return Integer.parseInt(args[0]);
        }

        String envPort = System.getenv("WORKER_PORT");
        if (envPort != null && !envPort.isBlank()) {
            return Integer.parseInt(envPort);
        }

        return 8081;
    }
}
