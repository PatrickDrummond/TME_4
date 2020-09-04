package tme3;


import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class EventClasses  {
    //    private boolean light = false;
//    private boolean water = false;
//    private boolean fans = false;
//    private boolean windowok = true;
//    private boolean poweron = true;
//    private String thermostat = "Day";
//    private String eventsFile = "examples1.txt";
    private int errorcode;


    public boolean finished = false;
    private long eventTime;
    protected final long delayTime;
    GreenhouseControls gc;

    public EventClasses(long delayTime, GreenhouseControls gc) {
        this.delayTime = delayTime;
        this.start();
        this.gc = gc;
    }

    public void start(){
        eventTime = System.currentTimeMillis() + delayTime;
    }

    public boolean ready(){
        return System.currentTimeMillis() >= eventTime;
    }


    public class LightOn extends EventClasses implements Event  {

        public LightOn(long delayTime, GreenhouseControls gc) {
            super(delayTime, gc);
        }

        public void action(GreenhouseControls gc) {
            // Put hardware control code here to
            // physically turn on the light.
            // light = true;
            TwoTuple<String, Boolean> tt = new TwoTuple<>("Light", true);
            gc.setVariable(tt);
            finished = true;
            //TODO: add finished  to all ev actions
        }

        public String toString() {
            return "Light is on";
        }

        @Override
        public void start() {
            eventTime = System.currentTimeMillis() + delayTime;
        }

        @Override
        public void run() {
            action(this.gc);
            System.out.println("From  LightON run() " + Thread.currentThread().getName());
        }
    }


    public class LightOff extends EventClasses implements Event {
        public LightOff(long delayTime, GreenhouseControls gc) { super(delayTime, gc); }

        public void action(GreenhouseControls gc) {
            // Put hardware control code here to
            // physically turn off the light.
            // light = false;
            TwoTuple<String, Boolean> tt = new TwoTuple<>("Light", false);
            gc.setVariable(tt);
        }
        public String toString() { return "Light is off"; }



        @Override
        public void run() {
            action(this.gc);
            System.out.println("From  LightOFF run() " + Thread.currentThread().getName());
        }
    }

    public class WaterOn extends EventClasses implements Event {
        public WaterOn(long delayTime, GreenhouseControls gc) { super(delayTime, gc); }
        public void action(GreenhouseControls gc) {
            // Put hardware control code here.
            // water = true;
            TwoTuple<String, Boolean> tt = new TwoTuple<>("Water", true);
            gc.setVariable(tt);
        }
        public String toString() {
            return "Greenhouse water is on";
        }
        public void run(){
            action(this.gc);
        }
    }

    public class WaterOff extends EventClasses implements Event {
        public WaterOff(long delayTime, GreenhouseControls gc) { super(delayTime, gc); }
        public void action(GreenhouseControls gc) {
            // Put hardware control code here.
            // water = false;
            TwoTuple<String, Boolean> tt = new TwoTuple<>("Water", false);
            gc.setVariable(tt);
        }
        public String toString() {
            return "Greenhouse water is off";
        }
        public void run(){
            action(this.gc);
        }
    }

    public class ThermostatNight extends EventClasses implements Event {
        public ThermostatNight(long delayTime, GreenhouseControls gc) {
            super(delayTime, gc);
        }
        public void action(GreenhouseControls gc) {
            // Put hardware control code here.
            //thermostat = "Night";
            TwoTuple<String, String> tt = new TwoTuple<>("Thermostat", "Night");
            gc.setVariable(tt);
        }
        public String toString() {
            return "Thermostat on night setting";
        }
        public void run(){
            action(this.gc);
        }
    }

    public class ThermostatDay extends EventClasses implements Event {
        public ThermostatDay(long delayTime, GreenhouseControls gc) {
            super(delayTime, gc);
        }
        public void action(GreenhouseControls gc) {
            // Put hardware control code here.
            // thermostat = "Day";
            TwoTuple<String, String> tt = new TwoTuple<>("Thermostat", "Day");
            gc.setVariable(tt);
        }
        public String toString() {
            return "Thermostat on day setting";
        }
        public void run(){
            action(this.gc);
        }
    }

    // Event class for FansOn
    public class FansOn extends EventClasses implements Event {
        public FansOn(long delayTime, GreenhouseControls gc) { super(delayTime, gc); }
        public void action(GreenhouseControls gc) {
            // Put hardware control code here.
            // fans = true;
            TwoTuple<String, Boolean> tt = new TwoTuple<>("Fans", true);
            gc.setVariable(tt);
        }
        public String toString() {
            return "Fans are on";
        }
        public void run(){
            action(this.gc);
        }
    }

    // Event class for FansOff
    public class FansOff extends EventClasses implements Event {
        public FansOff(long delayTime, GreenhouseControls gc) { super(delayTime, gc); }
        public void action(GreenhouseControls gc) {
            // Put hardware control code here.
            // fans = false;
            TwoTuple<String, Boolean> tt = new TwoTuple<>("Fans", false);
            gc.setVariable(tt);
        }
        public String toString() {
            return "Fans are off";
        }
        public void run(){
            action(this.gc);
        }
    }

    // Window Malfunction Event
    public class WindowMalfunction extends EventClasses implements Event {
        public WindowMalfunction(long delayTime, GreenhouseControls gc) { super(delayTime, gc); }
        public void action(GreenhouseControls gc) throws ControllerException, IOException {
            // Put hardware control code here.
            //  windowok = false;
            errorcode = 1;
            TwoTuple<String, Boolean> tt = new TwoTuple<>("windowok", false);
            gc.setVariable(tt);
            throw new ControllerException("Window Malfunction Event", 1);
        }
        public void run(){
            try {
                action(this.gc);
            } catch (ControllerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Power Out Event
    public class PowerOut extends EventClasses implements Event {
        public PowerOut(long delayTime, GreenhouseControls gc) { super(delayTime, gc); }
        public void action(GreenhouseControls gc) throws IOException, ControllerException {
            // Put hardware control code here.
            // poweron = false;
            errorcode = 2;
            TwoTuple<String, Boolean> tt = new TwoTuple<>("PowerOn", false);
            gc.setVariable(tt);
            throw new ControllerException("Power Out Event", 2);
        }
        public void run(){
            try {
                action(this.gc);
            } catch (ControllerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Bell extends EventClasses implements Event {
        public Bell(long delayTime, GreenhouseControls gc) {super(delayTime, gc);}
        public void action(GreenhouseControls gc) {
            // nothing to do
            // no TwoTuple as Bell is not a state
        }
        public void run() {
            action(this.gc);
        }
        public String toString() { return "Bing!"; }
    }

    public class PowerOn implements Fixable {
        public void fix() {
           // poweron = true; // turns power on
            errorcode = 0; //clears error code
            TwoTuple<String, Boolean> tt = new TwoTuple<>("PowerOn", true);
            gc.setVariable(tt);
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
          //  windowok = true; // fixes window problem
            errorcode = 0; //clears error code
            TwoTuple<String, Boolean> tt = new TwoTuple<>("windowok", true);
            gc.setVariable(tt);
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

    public class Terminate extends EventClasses implements Event {
        public Terminate(long delayTime, GreenhouseControls gc) { super(delayTime, gc); }
        public void action(GreenhouseControls gc) { System.exit(0); }
        public String toString() { return "Terminating";  }
        public void run(){
            action(this.gc);
        }
    }

    // Provide the means to create event classes from their names
    public Event getEvent (String name, long time) {

        Class outer = EventClasses.class; // fetches outer class
        Class inner = null;
        Constructor con = null;

        try {   // finds the inner class that we're looking to create
            inner = Class.forName("tme3.EventClasses$" + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Constructor[] cons = inner.getConstructors();

        try {
            con = inner.getConstructor(EventClasses.class, Long.TYPE, GreenhouseControls.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        // Now, make the event
        EventClasses ec = new EventClasses(time, this.gc);

        Object event = null;

        try {
            event = con.newInstance(ec, time, gc);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return (Event) event;

    }

    public class Restart extends EventClasses implements Event {

        String eventsFile;

        public Restart(long delayTime, String filename, GreenhouseControls gc) {
            super(delayTime, gc);
            eventsFile = filename;
        }

        public void action(GreenhouseControls gc) { // action() can only take GreenhouseControls as parameter
            //System.out.println("You're inside Restart.action");

            // Make a new file class with input file specified in command line

            int x = 1;

            //File myFile = null;

            File myFile = new File(eventsFile);

//            try {
//                myFile = new File(eventsFile);
//            } catch(Exception e){
//                e.printStackTrace();
//            }



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
                Long etime = Long.parseLong(eventTime);

                // call getEvent() to get events to add to eventList array
                Event e;

                e = getEvent(eventName, etime);


                // adding event to controller
                gc.c.addEvent(e);




            }
            myReader.close();

        }

        public void run(){
            action(this.gc);
        }   // run method from Event interface

        public String toString() {
            return "Restarting system";
        }
    }

}