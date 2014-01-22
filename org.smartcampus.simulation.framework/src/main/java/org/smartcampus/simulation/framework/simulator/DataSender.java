package org.smartcampus.simulation.framework.simulator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.smartcampus.simulation.framework.messages.SendValue;

/**
 * @inheritDoc
 * 
 *             This class allow to send a request HTTP
 */
public class DataSender extends DataMaker {

    public DataSender(final String url) {
        super(url);
    }

    @Override
    /**
     * @inheritDoc
     */
    public void onReceive(final Object o) throws Exception {
        if (o instanceof SendValue) {
            SendValue sendValue = (SendValue) o;
            StringBuilder obj = new StringBuilder();
            obj.append("{");
            obj.append("\"n\":");
            obj.append(sendValue.getName());
            obj.append(",\"v\":");
            obj.append(sendValue.getValue());
            obj.append(",\"t\":");
            obj.append(sendValue.getTime());
            obj.append("}");
            URL url = new URL(this.output);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setRequestMethod("POST");

            httpconn.setDoOutput(true);
            httpconn.setAllowUserInteraction(false);
            httpconn.setRequestProperty("charset", "utf-8");
            httpconn.setRequestProperty("Content-Length",
                    "" + Integer.toString(obj.toString().getBytes().length));

            httpconn.connect();
            DataOutputStream wr = new DataOutputStream(httpconn.getOutputStream());
            wr.writeBytes(obj.toString());
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    httpconn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            httpconn.disconnect();
        }
    }
}
