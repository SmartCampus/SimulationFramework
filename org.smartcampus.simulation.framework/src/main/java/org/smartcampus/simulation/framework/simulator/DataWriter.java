package org.smartcampus.simulation.framework.simulator;

import org.smartcampus.simulation.framework.messages.SendValue;

public class DataWriter extends DataMaker {
    @Override
    public void onReceive(final Object o) throws Exception {
        if (o instanceof SendValue) {
            SendValue sendValue = (SendValue) o;
            this.log.debug("J'ecris dans un fichier : { n : " + sendValue.getName()
                    + ", v :" + sendValue.getValue() + ", t:" + sendValue.getTime() + "}");
        }
    }
}
