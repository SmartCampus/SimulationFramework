package org.smartcampus.simulation.framework.simulator;

import java.util.HashMap;
import java.util.Map;

public abstract class FileFormator {
    /**
     * The path of the input file
     */
    private String input;

    /**
     * The parameters for the Subclasses
     */
    protected Map<String, String> params;

    public FileFormator() {
        this.params = new HashMap<String, String>();
    }

    /**
     * Handle the message InitReplayParam
     * 
     * @param value
     *            contains the initialization of the parameters of the simulation
     */
    protected final void initReplayParam(final String key, final String value) {
        this.params.put(key, value);
    }

    /**
     * @return the input
     */
    protected final String getInput() {
        return this.input;
    }

    /**
     * @param input
     *            the input to set
     */
    public void setInput(final String input) {
        this.input = input;
    }

    /**
     * Return the next frequency between the line n and n+1
     * 
     * @return the frequency in milliseconds
     */
    public abstract long getNextFrequency();

    /**
     * return the value of the next line
     * 
     * @return the value present at the next line
     */
    protected abstract Map<String, String> getNextValue();

    /**
     * closing the Input after reading the file
     */
    protected abstract void close();

    /**
     * going to the first line wanted in the file and returning the value of this line
     *
     * @return the value at this line
     */
    protected abstract void beginReplay();


    /**
     * Transform the strings in a TimeStamp in milliseconds
     * 
     * @param columns
     *            The strings corresponding to the TimeStamp
     * @return The TimeStamp in milliseconds
     */
    protected abstract long transform(String[] columns);


    /**
     * Return if the end of file is reached
     * 
     * @return return false if the end of file is reached, true otherwise
     */
    public abstract boolean hasNextLine();

}
