package kineticprocessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import processing.core.*;
import jssc.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Daniel Huth
 */

public class App extends PApplet
{
    private Window currentWindow;
    private boolean running;
    private HashMap<Character,Integer> keys;
    private HashMap<String,PImage> images;
    private boolean leftMousePressed;
    private boolean rightMousePressed;
    static SerialPort port;
    private Logger log;
    //private String input;
    private int gameMode;   
    private int points;
    private String username;
    private ArrayList<Point2D> diagram;
    
    static int currentVoltage;
    
    public final int SCREEN_WIDTH = 1080;
    public final int SCREEN_HEIGHT = 720;
    public final String HIGHSCORE_FILE = "data/scores.khsf";
    public final String TEXTURE_FILE = "data/textures.dat";
    
     /**
     * Initializes the App.
     * Loads Images, Font, the Logger, etc.
     * @return Success of initialization.
     */
    private boolean Init()
    {
        // Initialize Logger
        log = new Logger("data/log.txt");
        
        size(SCREEN_WIDTH,SCREEN_HEIGHT);
        frameRate(30);
        
        // Initialize the Key-System
        InitKeys();
        
        // Initialize the Serial
        if(!InitSerial()) return false;
        
        currentWindow = new SplashWindow(this);
                    
        // Load Textures
        LoadTextures(TEXTURE_FILE);
        
        // Load Font
        PFont fnt = createFont("data/Sketch_Block.ttf",32);
        if(fnt == null) 
        {
            log.Write("Could not load font!");
            Shutdown();
        }
        textFont(fnt);
        
        currentVoltage = 0;
        
        gameMode = -1;
        points = 0;
        
        return true;
    }
    
    /**
     * Initializes the InputSystem.
     *  
     * Basically fills the Key-HashMap.
     */
    private void InitKeys()
    {
        keys = new HashMap<>();
        // Fill the Hashmap with keys from 'a' to 'z' + some special charactes
        for(int i = 64; i < 64+59; i++) keys.put((char)i,0);
        keys.put('\b', 0);
        keys.put('\n', 0);        
    }
    
    /**
     * Initializes the Serial. 
     * 
     * @return Success of the initialization.
     */
    private boolean InitSerial()
    {
        String[] portNames = SerialPortList.getPortNames();
        port = new SerialPort(portNames[0]);
        try
        {            
            log.Write("Port opened: " + port.openPort());
            log.Write("Params setted: " + port.setParams(115200, 8, 1, 0));
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            port.setEventsMask(mask);//Set mask
            port.addEventListener(new SerialPortReader());
        }
        catch(SerialPortException ex)
        {
            log.Write("[ERROR] "+ex.getMessage());
            Shutdown();
        }
        return true;
    }
    
    /**
     * Loads all Textures from a specific file. 
     * 
     */
    private void LoadTextures(String filename)
    {
        images = new HashMap<>();
        // The whole content of the file in an arraylist.
        ArrayList<String> lines = readFile(filename);
        
        for(String line : lines)
        {
            // If line contains //, skip it. Commentaries.
            if(line.contains("//")) continue;
            
            String parts[] = line.split(";");
            
            LoadTexture(parts[0], parts[1]);
       }
    }
    
    /**
     * Shuts down all systems and closes the application.
     */
    public void Shutdown()
    {
        log.Write("Shutting down...");
        /*tP2Dry
        {
            port.closePort();
        }
        catch(SerialPortException ex)
        {
            log.Write("[ERROR] "+ex.getMessage());
        }*/
        
        exit();
    }
    
    /**
     * Override of the processing setup method. 
     * Starting point of the app.
     */
    @Override
    public void setup()
    {
        Init();
        running = true;
    }
    
    /**
     * Override of the processing draw method.
     * Calls the draw and update method of the current window.
     * Basically the gameloop.
     */
    @Override
    public void draw()
    {
        if(running == false)
        {
            Shutdown();
        }
        
        // Recieves data from the Serial
        //serialEvent();
        
        // The standart background for all windows.
        background(71,92,141);
        DrawImage("background1",0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
        
        currentWindow.Draw();
        int response = currentWindow.Update();
        ChangeState(response);
        
        textSize(20);
        fill(255);
        //text("FPS: "+String.valueOf((int)frameRate), 50, 50);
    }

    /**
     * Changes the current window based on the response from the last update method.
     * 
     * @param response response code of the last update method.
     */
    private void ChangeState(int response)
    {
        if(response == -1) running = false;
        if(response == 1) currentWindow = new MenuWindow(this);
        if(response == 2) currentWindow = new PreparationWindow(this);
        if(response == 3)
        {
            ArrayList<Module> modules = new ArrayList<>();
            modules.add(new BarModule(this,10,10,400,700));
            modules.add(new DiagramModule(this, 700, 80, 300, 500));            
            currentWindow = new GameWindow(this,modules);
        }
        if(response == 4)
        {
            currentWindow = new ScoreWindow(this);
        }
    }
        
    /**
     * Loads a texture and assigns it to a name that you can later use to draw it.
     * 
     * Closes the application if the texture was not found. 
     * For more information have a look at the log.txt
     * 
     * @param name The name you want to give the texture.
     * @param path The path to the texture you want to use.
     */
    public void LoadTexture(String name, String path)
    {
        PImage img = loadImage(path);
        if(img == null)
        {
            log.Write("[ERROR] Could not load image from path: "+path);
            Shutdown();
        }
        else
        {
            images.put(name, img);
            log.Write("Loaded image("+name+") succesfull from path: "+path);
        }
    }
    
    /**
    * Draws an texture on the screen with given coordinates.
    * 
    * Shuts down if no texture with this name was found.
    *   
    * @param ImgName The name you assigned to a texture earlier.
    * @param x The x coordinate.
    * @param y The y coordinate.
    */
    public void DrawImage(String ImgName, int x, int y)
    {
        if(images.containsKey(ImgName))  image(images.get(ImgName), x, y);
        else 
        {
            log.Write("[ERROR] There is no image with the ID: "+ImgName);
            Shutdown();
        }
    }
    
    /**
    * Draws an texture on the screen with given coordinates.
    * 
    * With width and height parameters you can scale/stretch the texture.
    * Shuts down if no texture with this name was found.
    *   
    * @param ImgName The name you assigned to a texture earlier.
    * @param x The x coordinate.
    * @param y The y coordinate.
    * @param w The width of the texture.
    * @param h The height of the texture.
    */
    public void DrawImage(String ImgName, int x, int y, int w, int h)
    {
        if(images.containsKey(ImgName))  image(images.get(ImgName), x, y, w, h);
        else 
        {
            log.Write("[ERROR] There is no image with the ID: "+ImgName);
            Shutdown();
        }
    }
    
    /**
     * Reads a whole file.
     * 
     * @param name The name of the file to read.
     * @return an Arraylist with lines.
     */
    public ArrayList<String> readFile(String name)
    {
        ArrayList<String> lines = new ArrayList<>();
        try
        {
            // Creates a BufferedReader.
            BufferedReader br = new BufferedReader(new FileReader(name));   
           
            // Reads a line and stores it in 'in'
            String in = br.readLine();
            // As long as there are lines with content 
            while(in != null)
            {   
                // Add the line to the ArrayList
                lines.add(in);
                // Read the next line
                in = br.readLine();
            }            
            // Close the FileStream
            br.close();
        }
        catch(IOException e ) 
        { 
            log.Write("[ERROR] "+e.getMessage());
            Shutdown();
        }
        return lines;
    }
    
    /**
     * Writes an ArrayList of lines to a file.
     * 
     * @param filename the name of the file.
     * @param lines The lines you want to write.
     */
    public void WriteFile(String filename, ArrayList<String> lines)
    {
        try 
        {
            FileWriter writer = new FileWriter(filename,false);
            for(String line : lines)
                {
                writer.write(line+"\n");
            }
  
            writer.flush();
            
        } catch (IOException ex) 
        {
            java.util.logging.Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
   
    /**
     * Reads the highscores from a file an stores them in an HashMap.
     *
     * @return An ArrayList of LinkedHashMaps with the highscores and their names.
     */
    public ArrayList<LinkedHashMap<String,Integer>> getHighscores()
    {
        // Creates a new ArrayList, that is returned at the end.
        ArrayList<LinkedHashMap<String,Integer>> highscores = new ArrayList<>();
        
        // Store all lines of the highscore file in an arraylist.
        ArrayList<String> lines = readFile(HIGHSCORE_FILE);
        
        if(lines.size() < 3) 
        {
            log.Write("[ERROR] The highscorefile has less than 3 lines.");
            Shutdown();
        }
        
        // Iterate through the first 3 lines
        for(int i = 0; i < 3; i++)
        {
            // Split the lines at ';' and save them in an array
            String[] parts = lines.get(i).split(";");
            
            // Creates a LinkedHashMap wich will be filled with Names and Points
            LinkedHashMap<String,Integer> data = new LinkedHashMap<>();
            // Iterate through every Highscore Entry
            for (String part : parts) 
            {
                // Split each entry and save both parts in an array.
                String[] elements = part.split(":");
                // Save the Username and his points in the LinkedHashMap
                data.put(elements[0], Integer.valueOf(elements[1])); 
            }
            // Add the LinkedHashMap with the highscores to the ArrayList
            highscores.add(data);
        }
        // Return the highscores
        return highscores;
    }
   
    /////////////////////
    // Input-Functions //
    /////////////////////
    
    @Override
    public void keyPressed()
    {
        keys.put(key, 1);   
    }
        
    @Override
    public void keyReleased()
    {
        keys.put(key, 0);   
    }
        
    /**
     * Checks if a key is pressed.
     * 
     * Shuts down if the key s not found.
     * 
     * @param k The key you want to check in character format. 
     * @return If the key is pressed or not.
     */
    public boolean IsKeyPressed(char k)
    {
        if(keys.containsKey(k))
        {
            return (keys.get(k) == 1);
        }
        else
        {
            log.Write("[ERROR] There is no key: "+k);
            Shutdown();
        }
        return false;
    }
  
    @Override
    public void mousePressed()
    {
        if(mouseButton == LEFT) leftMousePressed = true;
        else rightMousePressed = true;
    }
    
    @Override
    public void mouseReleased()
    {
        if(mouseButton == LEFT) leftMousePressed = false;
        else rightMousePressed = false;
    }
    
    /**
     * @return true if the left mouse is pressed
     */
    public boolean IsLeftMousePressed()
    {
        return leftMousePressed;
    }
     
    /**
     * @return true if the right mouse is pressed
     */
    public boolean IsRightMousePressed()
    {
        return rightMousePressed;
    }
        
    /**
     * Serial Port Listener. 
     * 
     * If an Serial-Event occurs, the sent voltage is read into currentVoltage.      * 
     */
    static class SerialPortReader implements SerialPortEventListener
    {
        public void serialEvent(SerialPortEvent event)
        {
            if(event.isRXCHAR()){
                    try
                    {
                        String d = port.readString();
                        //d = d.substring(0,2);
                        if(d != null && d.length() > 1 && (d.charAt(0) >= 48 && d.charAt(0) <= 57))
                        {
                            if(d.charAt(1) <= 57 && d.charAt(1) >= 48)
                            {
                                d = d.substring(0,2);
                            }
                            else
                            {
                                d = d.substring(0,1);
                            }                          
                            
                            currentVoltage = Integer.parseInt(d);
                            if(currentVoltage <= 12) currentVoltage = 0;
                            else currentVoltage -= 12;
                            //System.out.println(currentVoltage);
                        }
                    }
                    catch(SerialPortException ex)
                    {
                        System.out.println(ex);
                    }
            }
        }
    }
    
    ///////////
    // Tools //
    ///////////
    
    /**
     * Checks if a point is inside a rectangle.
     *  
     * Basically this method checks if the point is higher, lower, left or right of the rectangle.
     *  
     * @param x x coordinate of the rectangle.
     * @param y y coordinate of the rectangle.
     * @param w w of the rectangle.
     * @param h h of the rectangle.
     * @param pX x coordinate of the point.
     * @param pY y coordinate of the point.
     * 
     * @return true if the point is inside the rectangle
     */
    boolean collision(int x, int y, int w, int h, int pX, int pY)
    {
        if (pX > x && pX < x + w 
         && pY > y && pY < y + h)
        {
            return true;
        }
  
        return false;
    }
    
    
    /**
     * Converts a byte to an integer. 
     * 
     * This method is used by the Serial methods.
     *  
     * @param b the byte to convert.
     * @return the convertet interger.
     */
    public static int bytesToInt(byte[] b) 
    {
        final ByteBuffer bb = ByteBuffer.wrap(b);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getInt();
    }
    
    /**
    * Checks if it is possible to convert a string to an integer.
    * 
    * @param s String to check.
    * @return if the string can be converted to an integer.
    */
    public static boolean isInteger(String s) {
        try 
        { 
            Integer.parseInt(s); 
        } catch(NumberFormatException e) 
        { 
            return false; 
        }
        return true;
    }
    
    /////////////
    // Setters //
    /////////////
    
    public void SetUserName(String name)
    {
        username = name;
    }
    
    public void SetGameMode(int i)
    {
        gameMode = i;
    }
    
    public void SetPoints(int i)
    {
        points = i;
    }
    
    public void SetDiagram(ArrayList<Point2D> points)
    {
        diagram = points;
    }
    /////////////
    // Getters //
    /////////////
    
    public int getGameMode()
    {
        return gameMode;
    }
    
    public int getPoints()
    {
        return points;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public HashMap<Character,Integer> getKeys()
    {
        return keys;
    }    
    
    public Logger getLogger()
    {
        return log;
    }
        
    public Date getTime()
    {
        return Calendar.getInstance().getTime();
    }
    
    public ArrayList<Point2D> getDiagram()
    {
        return diagram;
    }
    
    public int getCurrentVoltage()
    {
        return currentVoltage;
    }
    
    public static void main(String args[]) 
    {
       PApplet.main(new String[] { "kineticprocessing.App" });
    }
}

