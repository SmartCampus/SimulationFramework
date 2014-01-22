package org.smartcampus.simulation.framework.simulator;

import org.smartcampus.simulation.framework.messages.SendValue;

/**
 * @inheritDoc
 * 
 *             This class allow to send a request HTTP
 */
public class DataSender extends DataMaker {
    @Override
    /**
     * @inheritDoc
     */
    public void onReceive(final Object o) throws Exception {
        if (o instanceof SendValue) {
            SendValue sendValue = (SendValue) o;
            this.log.debug("J'envoie au serveur : { n : " + sendValue.getName() + ", v :"
                    + sendValue.getValue() + ", t:" + sendValue.getTime() + "}");
        }
    }
}
