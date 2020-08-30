package tme3;


import java.io.*;
import java.util.*;


public class EventClasses extends Event {

    private boolean light = false;
    private boolean water = false;
    private boolean fans = false;
    private boolean windowok = true;
    private boolean poweron = true;
    private String thermostat = "Day";
    private String eventsFile = "examples1.txt";
    private int errorcode;


    public EventClasses(long delayTime) {
        super(delayTime);
    }

    public class LightOn extends Event {
        public LightOn(long delayTime) {
            super(delayTime);
        }

        public void action() {
            // Put hardware control code here to
            // physically turn on the light.
            light = true;
        }

        public String toString() {
            return "Light is on";
        }

        @Override
        public void run() {
            action();
            System.out.println("From  LightON run() " + Thread.currentThread().getName());
        }

    }


    public class LightOff extends Event {
        public LightOff(long delayTime) { super(delayTime); }
        public void action() {
            // Put hardware control code here to
            // physically turn off the light.
            light = false;
        }
        public String toString() { return "Light is off"; }

        @Override
        public void run() {
            action();
            System.out.println("From  LightOFF run() " + Thread.currentThread().getName());
        }
    }
    public class WaterOn extends Event {
        public WaterOn(long delayTime) { super(delayTime); }
        public void action() {
            // Put hardware control code here.
            water = true;
        }
        public String toString() {
            return "Greenhouse water is on";
        }
    }
    public class WaterOff extends Event {
        public WaterOff(long delayTime) { super(delayTime); }
        public void action() {
            // Put hardware control code here.
            water = false;
        }
        public String toString() {
            return "Greenhouse water is off";
        }
    }
    public class ThermostatNight extends Event {
        public ThermostatNight(long delayTime) {
            super(delayTime);
        }
        public void action() {
            // Put hardware control code here.
            thermostat = "Night";
        }
        public String toString() {
            return "Thermostat on night setting";
        }
    }
    public class ThermostatDay extends Event {
        public ThermostatDay(long delayTime) {
            super(delayTime);
        }
        public void action() {
            // Put hardware control code here.
            thermostat = "Day";
        }
        public String toString() {
            return "Thermostat on day setting";
        }
    }

    // Event class for FansOn
    public class FansOn extends Event {
        public FansOn(long delayTime) { super(delayTime); }
        public void action() {
            // Put hardware control code here.
            fans = true;
        }
        public String toString() {
            return "Fans are on";
        }
    }

    // Event class for FansOff
    public class FansOff extends Event {
        public FansOff(long delayTime) { super(delayTime); }
        public void action() {
            // Put hardware control code here.
            fans = false;
        }
        public String toString() {
            return "Fans are off";
        }
    }

    // Window Malfunction Event
    public class WindowMalfunction extends Event {
        public WindowMalfunction(long delayTime) { super(delayTime); }
        public void action() throws ControllerException, IOException {
            // Put hardware control code here.
            windowok = false;
            errorcode = 1;
            throw new ControllerException("Window Malfunction Event", 1);
        }
    }

    // Power Out Event
    public class PowerOut extends Event {
        public PowerOut(long delayTime) { super(delayTime); }
        public void action() throws IOException, ControllerException {
            // Put hardware control code here.
            poweron = false;
            errorcode = 2;
            throw new ControllerException("Power Out Event", 2);

        }
    }

    public class Bell extends Event {

        public Bell(long delayTime) {super(delayTime);}
        public void action() {
            // nothing to do
        }
        public String toString() { return "Bing!"; }
    }

    public class PowerOn implements Fixable {
        public void fix() {
            poweron = true; // turns power on
            errorcode = 0; //clears error code
        }

        public void log() throws IOException {
            // logs to a text file called fix.log: time and nature of fix
            File file = new File("fix.log");  // File object to link to fix.log
            FileWriter fw = new FileWriter(file, false);   //FileWriter object fw
            PrintWriter pw = new PrintWriter(fw); //PrintWriter object pw
            pw.println("New Fix: Greenhouse Power Turned Back On " + "time: " + System.currentTimeMillis());
            System.out.println("New Fix: Greenhouse Power Turned Back On " + "time: " + System.currentTimeMillis());
            pw.close();
        }
    }

    public class FixWindow implements Fixable {
        public void fix() {
            windowok = true; // fixes window problem
            errorcode = 0; //clears error code
        }

        public void log() throws IOException {
            // logs to a text file called fix.log: time and nature of fix
            File file = new File("fix.log");  // File object to link to fix.log
            FileWriter fw = new FileWriter(file, false);   //FileWriter object fw
            PrintWriter pw = new PrintWriter(fw); //PrintWriter object pw
            pw.println("New Fix: Greenhouse Window Repaired " + "time: " + System.currentTimeMillis());
            System.out.println("New Fix: Greenhouse Window Repaired " + "time: " + System.currentTimeMillis());
            pw.close();
        }
    }

    public class Terminate extends Event {
        public Terminate(long delayTime) { super(delayTime); }
        public void action() { System.exit(0); }
        public String toString() { return "Terminating";  }
    }

    public class Restart extends Event {
        public Restart(long delayTime, String filename) {
            super(delayTime);
            eventsFile = filename;
        }
        public void action() {
            //System.out.println("You're inside Restart.action");


            // Make a new file class with input file specified in command line
            File myFile = new File(eventsFile);

            // Remind user what file they input
            // System.out.println("File name: " + myFile.getName());

            // New scanner to read input file
            Scanner myReader = null;
            try {
                myReader = new Scanner(myFile);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] eventsArray;
                eventsArray = data.split(",");
                String eventName = eventsArray[0].split("=")[1];
                String eventTime = eventsArray[1].split("=")[1];
                //System.out.println("Name" + eventName);
                //System.out.println("Time" + eventTime);
                int etime = Integer.parseInt(eventTime);

                // call getEvent() to get events to add to eventList array

                switch(eventName)
                {
                    case "LightOn":
                        addEvent(new LightOn(etime));
                        break;
                    case "LightOff":
                        addEvent(new LightOff(etime));
                        break;
                    case "WaterOn":
                        addEvent(new WaterOn(etime));
                        break;
                    case "WaterOff":
                        addEvent(new WaterOff(etime));
                        break;
                    case "ThermostatDay":
                        addEvent(new ThermostatDay(etime));
                        break;
                    case "ThermostatNight":
                        addEvent(new ThermostatNight(etime));
                        break;
                    case "FansOn":
                        addEvent(new FansOn(etime));
                        break;
                    case "FansOff":
                        addEvent(new FansOff(etime));
                        break;
                    case "Terminate":
                        addEvent(new GreenhouseControls.Terminate(etime));
                        break;
                    case "WindowMalfunction":
                        addEvent(new WindowMalfunction(etime));
                        break;
                    case "PowerOut":
                        addEvent(new PowerOut(etime));
                        break;
                    case "Bell":
                        // Bell specifies Rings
                        if (eventsArray.length == 3){
                            int rings = Integer.parseInt(eventsArray[2].split("=")[1]);
                            for (int i = 0; i< rings; i++){
                                addEvent(new Bell(etime + 2000 * i));
                            }
                        }
                        else {
                            addEvent(new Bell(etime));
                        }


                }

            }
            myReader.close();

        }

        public String toString() {
            return "Restarting system";
        }
    }




}