package org.smartcampus.simulation.framework.simulator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.smartcampus.simulation.framework.messages.CountRequestsPlusOne;
import org.smartcampus.simulation.framework.messages.CountResponsesPlusOne;
import org.smartcampus.simulation.framework.messages.SendValue;
import akka.actor.ActorSelection;

/**
 * @inheritDoc
 * 
 *             This class allow to send a request HTTP
 */
public class DataSender extends DataMaker {

    private ActorSelection counter;

    public DataSender(final String output) {
        super(output);
        this.counter = this.getContext().actorSelection(
                "/user/SimulationControlor/CounterResponses");
    }

    @Override
    /**
     * @inheritDoc
     */
    public void onReceive(final Object o) throws Exception {
        if (o instanceof SendValue) {
            SendValue sendValue = (SendValue) o;
            StringBuilder obj = new StringBuilder();
            obj.append("{").append("\"n\":").append(sendValue.getName())
                    .append(",\"v\":").append(sendValue.getValue()).append(",\"t\":")
                    .append(sendValue.getTime()).append("}");
            URL url = new URL(this.output);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();

            httpconn.setRequestMethod("POST");

            httpconn.setDoOutput(true);
            httpconn.setAllowUserInteraction(false);
            httpconn.setRequestProperty("charset", "utf-8");
            httpconn.setRequestProperty("Content-Length",
                    "" + Integer.toString(obj.toString().getBytes().length));
            httpconn.setRequestProperty("Content-Type", "application/json");

            httpconn.connect();
            DataOutputStream wr = new DataOutputStream(httpconn.getOutputStream());
            wr.writeBytes(obj.toString());
            wr.flush();
            wr.close();

            this.counter.tell(new CountRequestsPlusOne(), this.getSelf());

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    httpconn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            if (httpconn.getResponseCode() != 201) {
                this.log.debug("BAD ------------------" + httpconn.getResponseMessage());
            }
            else {
                this.counter.tell(new CountResponsesPlusOne(), this.getSelf());
            }

            httpconn.disconnect();

        }
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }

}
