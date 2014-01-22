package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;

/**
 * The InitOutput message allows the setting the url of the file name of the DataMakers
 */
public class InitOutput implements Serializable {
    private static final long serialVersionUID = 6546648934196676095L;
    private final String output;

    public InitOutput(final String s) {
        this.output = s;
    }

    /**
     * @return the output
     */
    public String getOutput() {
        return this.output;
    }

}
