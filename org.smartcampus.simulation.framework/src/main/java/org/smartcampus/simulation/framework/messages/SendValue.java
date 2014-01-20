package org.smartcampus.simulation.framework.messages;

/**
 * The SendValue message gives a name, a value and a time to a DataSender.
 */
public class SendValue {
    private final String name;
    private final String value;
    private final int    time;

    public SendValue(final String name, final String value, final int time) {
        this.name = name;
        this.value = value;
        this.time = time;
    }

    public String getName() {
        return this.name;
    }

    public int getTime() {
        return this.time;
    }

    public String getValue() {
        return this.value;
    }
}
