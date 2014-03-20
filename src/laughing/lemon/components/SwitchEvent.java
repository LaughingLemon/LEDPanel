package laughing.lemon.components;

import java.util.EventObject;

//subclass of standard event to use with the toggle switch
public class SwitchEvent extends EventObject {

    //state of switch
    private boolean switchedOn;

    //getter for switch state
    public boolean isSwitchedOn() {
        return switchedOn;
    }

    //constructor
    public SwitchEvent(Object source, boolean switchOn) {
        super(source);
        this.switchedOn = switchOn;
    }

}
