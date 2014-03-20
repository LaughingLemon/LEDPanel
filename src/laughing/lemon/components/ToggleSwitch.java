package laughing.lemon.components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

//Created by Shaun
public class ToggleSwitch extends Panel {

    private boolean switchOn = false;

    public boolean isSwitchOn() {
        return switchOn;
    }

    public void setSwitchOn(boolean switchOn) {
        if(this.switchOn != switchOn) {
            this.switchOn = switchOn;
            //tell the listeners the value has
            //been changed
            fireSwitchListener();
            //only repaint if the value has
            //been changed
            repaint();
        }
    }

    //custom event maintenance
    //this is a list of events that have to
    //be serviced when something happens
    private List<SwitchListener> listenerList = new ArrayList<SwitchListener>();

    public void addSwitchListener(SwitchListener listener) {
        listenerList.add(listener);
    }

    public void removeSwitchListener(SwitchListener listener) {
        listenerList.remove(listener);
    }

    private void fireSwitchListener() {
        //parcel up the state of the switch in a SwitchEvent object
        SwitchEvent e = new SwitchEvent(this, isSwitchOn());
        //then send it to every listener in the list
        for(SwitchListener listener : listenerList) {
            listener.switchChange(e);
        }
    }

    //internal dimensions of switch
    //used to figure out switch "hotspots"
    private int border;
    private int switchWidth;
    private int switchHeight;

    //extension of MouseAdapter
    //http://docs.oracle.com/javase/7/docs/api/java/awt/event/MouseAdapter.html
    //allows the component to respond to mouse clicks
    private class SwitchClick extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if(e.getX() >= border &&
                    e.getX() <= (border + switchWidth) &&
                    e.getY() >= border &&
                    e.getY() <= (border + switchHeight)) {
                //if it's the top half of the component
                //it's being switched on
                setSwitchOn(true);
            } else if(e.getX() >= border &&
                    e.getX() <= (border + switchWidth) &&
                    e.getY() >= (border + switchHeight) &&
                    e.getY() <= (switchHeight * 2) + border) {
                //if it's the bottom half of the component
                //it's being switched off
                setSwitchOn(false);
            }
        }
    }

    public ToggleSwitch() {
        addMouseListener(new SwitchClick());
    }

    public void paint(Graphics g) {
        //convert (up-cast) the Graphics object to
        //a Graphics2D object
        Graphics2D g2 = (Graphics2D) g;

        //resize the width so that it's always half the height
        int width = (int) (this.getHeight() * 0.5);
        int height = this.getHeight();
        this.setSize(width, height);

        //calculate the internal dimensions for
        //the switch "hotspots"
        border = (int) (width * 0.1);
        switchWidth = width - (border * 2);
        switchHeight = (height / 2) - border;

        if(isSwitchOn()) {
            //if the switch is on, the bottom half is light grey
            //with a dark grey band below to give it a crude 3D
            //look
            g2.setColor(Color.lightGray);
            g2.fillRect(border, height / 2, switchWidth, switchHeight - border);
            g2.setColor(Color.darkGray);
            g2.fillRect(border, switchHeight * 2, switchWidth, border);
            //
            g2.setColor(Color.red);
            g2.fillOval(switchWidth / 2, switchHeight / 2, border * 2, border * 2);

            g2.setColor(Color.black);
            g2.drawRect(border, height / 2, switchWidth, switchHeight);
            g2.drawRect(border, height - (border * 2), switchWidth, border);
            g2.drawOval(switchWidth / 2, switchHeight / 2, border * 2, border * 2);
        } else {
            g2.setColor(Color.darkGray);
            g2.fillRect(border, border, switchWidth, border);
            g2.setColor(Color.lightGray);
            g2.fillRect(border, border * 2, switchWidth, switchHeight - border);

            g2.setColor(Color.black);
            g2.fillOval(switchWidth / 2, switchHeight / 2, border * 2, (int) ((border * 2) * 0.9));
            g2.drawRect(border, border * 2, switchWidth, switchHeight - border);
            g2.drawRect(border, border, switchWidth, border);
        }

        g2.drawRect(0, 0, width - 1, height - 1);
        g2.drawRect(border, border, switchWidth, switchHeight * 2);
    }
}
