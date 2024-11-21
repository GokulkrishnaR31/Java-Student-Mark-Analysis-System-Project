import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;

public class StartServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        StudentMarkAnalysis analysis = new StudentMarkAnalysis();

        // Endpoint for inserting student marks
        server.createContext("/api/insert", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                    JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                    String name = requestBody.getString("studentName");

                    // Convert JSONArray to List<Integer>
                    List<Integer> marks = new ArrayList<>();
                    requestBody.getJSONArray("marks").forEach(item -> {
                        marks.add(((Number) item).intValue());  // Convert Object to Integer
                    });

                    analysis.insertStudentMarks(name, marks);

                    String response = "{\"status\":\"success\"}";
                    exchange.sendResponseHeaders(200, response.length());
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            }
        });

        // Endpoint for calculating statistics
        server.createContext("/api/calculateStatistics", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                JSONObject responseJson = analysis.calculateStatistics();
                String response = responseJson.toString();
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        });

        // Endpoint for getting top performer
        server.createContext("/api/topPerformer", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                JSONObject topPerformer = analysis.getTopPerformer();
                String response = topPerformer.toString();
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        });

        // Endpoint for getting lowest performer
        server.createContext("/api/lowestPerformer", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                JSONObject lowestPerformer = analysis.getLowestPerformer();
                String response = lowestPerformer.toString();
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        });

        // Endpoint for login
        server.createContext("/api/login", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                    JSONObject requestBody = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                    String username = requestBody.getString("username");
                    String password = requestBody.getString("password");
                    String role = requestBody.getString("role");

                    // Here, implement your login logic. For simplicity, assuming login is always successful
                    JSONObject response = new JSONObject();
                    response.put("status", "success");

                    exchange.sendResponseHeaders(200, response.toString().length());
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.toString().getBytes());
                    }
                }
            }
        });

        // Serve static files from the frontend directory
        serveStaticFiles(server);

        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8000");
    }

    private static void serveStaticFiles(HttpServer server) {
        server.createContext("/", exchange -> {
            String requestedFile = exchange.getRequestURI().getPath().substring(1);
            if (requestedFile.isEmpty()) {
                requestedFile = "login.html"; // Default file to login.html
            }

            File file = new File("frontend/" + requestedFile);
            if (file.exists()) {
                exchange.sendResponseHeaders(200, file.length());
                try (FileInputStream fis = new FileInputStream(file);
                     OutputStream os = exchange.getResponseBody()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                exchange.sendResponseHeaders(404, -1); // Not Found
            }
        });
    }
}
