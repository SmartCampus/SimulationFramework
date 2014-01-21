package org.smartcampus.simulation.framework.simulator;

import org.smartcampus.simulation.framework.messages.SendValue;

public class DataSender extends DataMaker {
    @Override
    public void onReceive(final Object o) throws Exception {
        if (o instanceof SendValue) {
            SendValue sendValue = (SendValue) o;
            this.log.debug("J'envoie au serveur : { n : " + sendValue.getName() + ", v :"
                    + sendValue.getValue() + ", t:" + sendValue.getTime() + "}");
        }
    }
}
