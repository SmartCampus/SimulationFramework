import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Temporary server for faking the middleware
 * 
 */
public class ServeurWeb {

    static Socket s;
    public static int numPort = 8000;

    public static void main(final String[] args) throws Exception {
        HttpServer server = HttpServer
                .create(new InetSocketAddress(numPort), 0);
        server.createContext("/value", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();

    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(final HttpExchange t) throws IOException {
            InputStream is = t.getRequestBody();

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            String s = sb.toString();

            try {
                JSONObject jsonObject = new JSONObject(s);
                System.out.println(jsonObject);
                this.sendResponse(t, 200);

            } catch (Exception e) {
                this.sendResponse(t, 404);
            }

        }

        private void sendResponse(final HttpExchange t, final int res) {
            String response = "This is the response";
            try {
                t.sendResponseHeaders(res, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
