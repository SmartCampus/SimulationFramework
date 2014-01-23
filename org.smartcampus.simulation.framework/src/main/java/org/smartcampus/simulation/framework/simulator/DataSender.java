package org.smartcampus.simulation.framework.simulator;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.smartcampus.simulation.framework.messages.SendValue;

/**
 * @inheritDoc
 * 
 *             This class allow to send a request HTTP
 */
public class DataSender extends DataMaker {
    HttpURLConnection httpconn;

    public DataSender(final String output) {
        super(output);
        URL url;
        try {
            url = new URL(this.output);
            this.httpconn = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
            this.httpconn.setRequestMethod("POST");

            this.httpconn.setDoOutput(true);
            this.httpconn.setAllowUserInteraction(false);
            this.httpconn.setRequestProperty("charset", "utf-8");
            this.httpconn.setRequestProperty("Content-Length",
                    "" + Integer.toString(obj.toString().getBytes().length));
            this.httpconn.setRequestProperty("Content-Type", "application/json");

            this.httpconn.connect();
            DataOutputStream wr = new DataOutputStream(this.httpconn.getOutputStream());
            wr.writeBytes(obj.toString());
            wr.flush();
            wr.close();

            // this.log.debug("" + this.httpconn.getResponseCode());
            // this.log.debug(this.httpconn.getResponseMessage());

            if (this.httpconn.getResponseCode() != 201) {
                this.log.debug("BAD ------------------"
                        + this.httpconn.getResponseMessage());
            }
        }
    }

    @Override
    public void postStop() throws Exception {
        this.httpconn.disconnect();
        super.postStop();
    }

}
