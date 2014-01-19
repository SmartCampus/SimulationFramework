package org.smartcampus.simulation.framework.messages;

/**
 * Created by foerster on 19/01/14.
 */
public class SendValue {
    private final String name;
    private final String value;
    private final String time;

    public SendValue(String name,String value,String time){
        this.name = name;
        this.value = value;
        this.time = time ;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getTime() {
        return time;
    }
}
