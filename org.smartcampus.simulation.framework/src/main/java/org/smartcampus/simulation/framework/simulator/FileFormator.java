package org.smartcampus.simulation.framework.simulator;

import java.util.HashMap;
import java.util.Map;
import org.smartcampus.simulation.framework.messages.InitReplayParam;

public abstract class FileFormator {

    /**
     * The path of the input file
     */
    private String input;

    /**
     * the number of the line that we have to read
     */
    protected int nbLineToRead;

    /**
     * The parameters for the Subclasses
     */
    protected Map<String, Object> params;

    public FileFormator() {
        this.params = new HashMap<String, Object>();
    }

    /**
     * return the value of the next line
     * 
     * @return the value present at the next line
     */
    protected abstract String getNextValue();

    /**
     * return the number of line in the input file
     */
    protected abstract int getNbLine();

    /**
     * closing the Input after reading the file
     */
    protected abstract void close();

    /**
     * going to the first line wanted in the file and returning the value of this line
     * 
     * @param firstLine
     *            the number of the first line of the file
     * @return the value at this line
     */
    protected abstract String beginReplay(int firstLine);

    /**
     * Handle the message InitReplayParam
     * 
     * @param message
     *            contains the initialization of the parameters of the simulation
     */
    private void initReplayParam(final InitReplayParam message) {
        this.params.put(message.getKey(), message.getValue());
    }

    /**
     * @return the input
     */
    protected String getInput() {
        return this.input;
    }

    /**
     * @param input
     *            the input to set
     */
    public void setInput(final String input) {
        this.input = input;
    }

}
