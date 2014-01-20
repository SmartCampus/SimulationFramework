package org.smartcampus.simulation.framework.messages;

/**
 * Created by foerster on 19/01/14.
 */
public class SendValue {
    private final String name;
    private final String value;
    private final String time;

    public SendValue(final String name, final String value, final String time) {
        this.name = name;
        this.value = value;
        this.time = time;
    }

    public String getName() {
        return this.name;
    }

    public String getTime() {
        return this.time;
    }

    public String getValue() {
        return this.value;
    }
}
