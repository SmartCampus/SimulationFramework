package org.smartcampus.simulation.framework.simulator;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.smartcampus.simulation.framework.messages.CountRequestsPlusOne;
import org.smartcampus.simulation.framework.messages.CountResponsesPlusOne;
import org.smartcampus.simulation.framework.messages.SendValue;

import java.io.*;
import java.net.InetSocketAddress;

public class DataSenderTest {

    private static ActorSystem system;
    private static HttpServer server;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("AkkaTestModele");

        try {
            server = HttpServer.create(new InetSocketAddress(8888), 0);
            server.createContext("/test/server", new MyHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (IOException e) {
            System.err.println("Le serveur n'a pas réussi à se lancer : ");
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void teardown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
        server.stop(1);
    }

    @Test
    public void testDataSender() {
        new JavaTestKit(system) {
            {
                final ActorRef dataSender = system.actorOf(Props.create(DataSender.class,
                        "http://localhost:8888/test/server"));

                dataSender.tell(new SendValue("Name", "Value", 123456789), this.getRef());
                this.expectMsgClass(duration("1 second"), CountRequestsPlusOne.class);
                this.expectMsgClass(duration("1 second"), CountResponsesPlusOne.class);

                dataSender.tell("hello", this.getRef());
                this.expectNoMsg(duration("1 second"));
            }
        };
    }

    public static class MyHandler implements HttpHandler {
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
                JSONObject expected = new JSONObject("{\"n\":\"Name\",\"v\":\"Value\",\"t\":\"123456\"}");
                JSONObject actual = new JSONObject(s);
                // Test du message envoyé au serveur
                Assert.assertEquals(expected.get("n"), actual.get("n"));
                Assert.assertEquals(expected.get("v"), actual.get("v"));
                Assert.assertEquals(expected.get("t"), actual.get("t"));
                this.sendResponse(t, 201);
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
