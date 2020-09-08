package tme3;

import java.io.*;
import tme3.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Date;

public class ControllerException extends Throwable {

    // errorcode and message thrown from PowerOut and WindowMalfunction events
    public int errorCode;
    public String errorMessage;
    GreenhouseControls gc;

    // these 2 variables and 1 object use java libraries to get the system date into a readable format
    //Date object
    Date date= new Date();
    //getTime() returns current time in milliseconds
    long time = date.getTime();
    //Passed the milliseconds to constructor of Timestamp class
    Timestamp ts = new Timestamp(time);


    public ControllerException(String errorMessage, int errorCode, GreenhouseControls gc) throws IOException {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.gc = gc;

        // Writing to error.log file

        File file = new File("error.log");  // File object to link to error.log
        FileWriter fw = new FileWriter(file, true);   //FileWriter object fw
        PrintWriter pw = new PrintWriter(fw); //PrintWriter object pw
        pw.println("System Shutdown " + errorMessage + " errorcode: " + errorCode + " time: " + ts);
        System.out.println("System Shutdown " + errorMessage + " errorcode: " + errorCode + " time: " + ts);
        pw.close();

        //public void shutdown(){
            // the shutdown method must also serialize the current GreenhouseObject
            if (errorCode == 0) {
                System.out.println("Greenhouse Operations Successful. Shutting Down.");
            }

            if (errorCode == 1) {
                System.out.println("Window Malfunction, Greenhouse Shutting Down");
                serialize(gc);
            }
            if (errorCode == 2) {
                System.out.println("Power Out, Greenhouse Shutting Down");
                serialize(gc);
            }
        }

        //TODO: Thread class can't implement serializeable ?
    public void serialize(GreenhouseControls gc) {
        // Serialize and save entire tme3.GreenhouseControls object into file dump.out
        System.out.println("Inside ControllerException.serialize");
        int asdf = 1;
        try {
            FileOutputStream f = new FileOutputStream(new File("dump.out"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write Objects to File
            o.writeObject(gc);

            o.close();  // must close the file or nothing gets written
            f.close();
            System.out.println("Serialized Data Saved in dump.out file");

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }

    }



}
