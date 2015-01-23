/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kineticprocessing;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;

/**
 * Logger dir einen!
 */
public class Logger 
{
    private FileWriter writer;
    private Calendar cal;
    private SimpleDateFormat sdf;   
    
    /**
     * Creates a new Logger.  
     * 
     * @param file The file name. 
     */
    public Logger(String file)
    {
        try 
        {
            writer = new FileWriter(file,false);
            cal = Calendar.getInstance();	
            sdf = new SimpleDateFormat("HH:mm:ss");
        } 
        catch (IOException ex) 
        {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Writes an message to the logfile. 
     * 
     * @param text The text to write.
     */
    public void Write(String text)
    {
        try 
        {
            String toWrite = "["+sdf.format(cal.getTime())+"] " + text + "\n";
            writer.write(toWrite);
            System.out.print(toWrite);  // Debug information...
            writer.flush();
        } 
        catch (IOException ex) 
        {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
