package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;

/**
 * The SendValue message gives a name, a value and a time to a DataSender.
 */
public class SendValue implements Serializable {
    private static final long serialVersionUID = 6205825964596201259L;
    private final String      name;
    private final String      value;
    private final long        time;

    public SendValue(final String name, final String value, final long time) {
        this.name = name;
        this.value = value;
        this.time = time;
    }

    public String getName() {
        return this.name;
    }

    public long getTime() {
        return this.time;
    }

    public String getValue() {
        return this.value;
    }


}
