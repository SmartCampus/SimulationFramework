package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;

/**
 * The InitInput message allows the setting the path of the file from witch datas will be
 * replay
 */
public class InitInput implements Serializable {
    private static final long serialVersionUID = -7764289777614670403L;
    private final String input;

    public InitInput(final String s) {
        this.input = s;
    }

    /**
     * @return the input
     */
    public String getInput() {
        return this.input;
    }

}
