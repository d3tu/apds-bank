import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class Main {
  private static String wwwroot = "wwwroot";
  private static Database database;

  public static void main(String[] args) throws Exception {
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    HttpContext context = server.createContext("/");
    context.setHandler(Main::onRequest);
    server.start();
    database = new Database("database.sqlite");
    database.execute(Database.Utils.useSql("user", "prepare"));
  }
  
  private static void onRequest(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod();
    URI uri = exchange.getRequestURI();
    String path = uri.getPath();

    System.out.println("[" + method + "]: " + path);

    if ("GET".equals(method)) {
      String filePath;
      if ("/".equals(path)) filePath = "/index.html";
      else if ("/sign-in".equals(path)) filePath = "/sign-in.html";
      else if ("/sign-up".equals(path)) filePath = "/sign-up.html";
      else filePath = path;

      File file = new File(wwwroot + filePath).getCanonicalFile();

      if ("/".equals(path) || "/sign-in".equals(path) || "/sign-up".equals(path)) {
        sendFile(exchange, file);
      } else if (file.isFile() && !file.getName().endsWith(".html")) {
        sendFile(exchange, file);
      } else {
        exchange.sendResponseHeaders(404, -1);
      }
    } else if ("POST".equals(method)) {
      if ("/sign-up".equals(path)) {
        // String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
        // String boundary = ContentType.parse(contentType).getParameter("boundary");

        InputStream is = exchange.getRequestBody();
        OutputStream os = exchange.getResponseBody();

        final byte[] bytes = is.readAllBytes();
        String body = new String(bytes);
        System.out.println(body);

        Map<String, String> params = splitQuery(body);
        // MultiPart p = new MultiPart(body);
        // Map<String, String> params = p.getParameters();

        String name = params.get("name");
        String email = params.get("email");
        String cpf = params.get("cpf");
        String password = params.get("password");

        try {
          database.executeUpdate(Database.Utils.useSql(
            "user", "insert"
          ),
            "name", "email", "cpf", "password",
            name, email, cpf, password
          );
        } catch (Exception e) {
          e.printStackTrace();
        }

        exchange.sendResponseHeaders(200, bytes.length);

        os.write(bytes);

        is.close();
        os.close();
      }
    } else if ("HEAD".equals(method)) {
      exchange.sendResponseHeaders(200, -1);
    } else {
      exchange.sendResponseHeaders(405, -1);
    }
  }

  public static void sendFile(HttpExchange exchange, File file) throws IOException {
    Headers headers = exchange.getResponseHeaders();
    
    try {
      headers.set("Content-Type", Files.probeContentType(file.toPath()));
    } catch (Exception e) {
      headers.set("Content-Type", "application/json");
    }
    
    exchange.sendResponseHeaders(200, 0);

    OutputStream os = exchange.getResponseBody();
    FileInputStream fs = new FileInputStream(file);
    final byte[] buffer = new byte[0x10000];
    int count = 0;
    
    while ((count = fs.read(buffer)) >= 0) {
      os.write(buffer, 0, count);
    }

    fs.close();
    os.close();
  }

  public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
    Map<String, String> query_pairs = new LinkedHashMap<String, String>();
    String[] pairs = query.split("&");
    for (String pair : pairs) {
      int idx = pair.indexOf("=");
      query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
    }
    return query_pairs;
  }
}
