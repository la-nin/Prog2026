package worker.http;

import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;


public class WorkerRouter {
    public static final Random RANDOM = new Random();

    public HttpResponse route(HttpRequest request) {

        if (!"GET".equals(request.method())) {
            return HttpResponse.methodNotAllowed();
        }

        return switch (request.path()) {
                case "/health" -> HttpResponse.ok("OK");
                case "/work" -> {
                    int n = 100000 + RANDOM.nextInt(400001);
                    long total = 0;

                    for (int i = 0; i < n; i++) {
                        total += i;
                    }

                    yield HttpResponse.ok("N = " + n + " Sum: " + total);
                }
                default -> HttpResponse.notFound();
            };

    }
    }

