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

    // these 2 variables and 1 object use java libraries to get the system date into a readable format
    //Date object
    Date date= new Date();
    //getTime() returns current time in milliseconds
    long time = date.getTime();
    //Passed the milliseconds to constructor of Timestamp class
    Timestamp ts = new Timestamp(time);


    public ControllerException(String errorMessage, int errorCode) throws IOException {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;

        // Writing to error.log file

        File file = new File("error.log");  // File object to link to error.log
        FileWriter fw = new FileWriter(file, true);   //FileWriter object fw
        PrintWriter pw = new PrintWriter(fw); //PrintWriter object pw
        pw.println("System Shutdown " + errorMessage + " errorcode: " + errorCode + " time: " + ts);
        System.out.println("System Shutdown " + errorMessage + " errorcode: " + errorCode + " time: " + ts);
        pw.close();

    }

}
