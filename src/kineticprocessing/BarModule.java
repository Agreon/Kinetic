/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kineticprocessing;

import java.awt.Rectangle;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author daniel
 */
public class BarModule extends Module
{
    private LinkedHashMap<String,Integer> highscores;
    private SimpleEntry<String,Integer> target;
    private int max_bar_height;
    
    public BarModule(App app, int x, int y, int w, int h)
    {
        this.app = app;
        rect = new Rectangle(x,y,w,h);
        highscores = app.getHighscores().get(app.getGameMode()-1);     
        max_bar_height = rect.height - 220;  
        
        for(String key : highscores.keySet())
        {            
            target = new SimpleEntry(key,highscores.get(key));  
            break;
        }
    }
    
    @Override
    void Update() 
    {
        //app.SetPoints(app.getPoints() + 10);
    }

    @Override
    void Draw()
    {
        // How many percent of the highscore is the player value?
        double percent = (double)app.getPoints() / ((double)target.getValue() / 100);            
        int playerHeight = (int)(max_bar_height*percent)  / 100;
        if(playerHeight > max_bar_height) playerHeight = max_bar_height;
        
        app.translate(0, -170);
        
        DrawUserBar(playerHeight,percent);          
        DrawHighscoreBar(playerHeight);
        
        app.translate(0, 170);
       
    }
    
    void DrawUserBar(int playerHeight, double percent)
    {
        int x = rect.x + 230;  
        int y = rect.height - playerHeight + 10;
        int w = 150;
        
        // Userbar
        for (int i = y; i <= y+playerHeight; i++) 
        {
            float inter = app.map(i, y, y+playerHeight, 0, 1);
            app.stroke(app.lerpColor(app.color(255,255,255), app.color(255,255,255,0), inter));
            app.line(x, i, x+w, i);
        }
        
        app.fill(0);
               
        // Border
        int sWidth = 3;
        //Left
        app.rect(x-sWidth, y-sWidth, sWidth, playerHeight+sWidth+1);
        // Top
        app.rect(x-sWidth, y-sWidth, w+sWidth*2, sWidth);
        // Right
        app.rect(x+w, y-sWidth, sWidth, playerHeight+sWidth+1);
        // Bottom
        app.rect(x-sWidth, y+playerHeight, w+sWidth*2, sWidth);
        
        app.strokeWeight(2);
        app.textSize(20);
        app.fill(0);
       
        // If the bar is high enough, draw the points of the player.  
        if(rect.height - playerHeight + 60  < rect.y + rect.height)  
        {
            app.text("YOU", 245, rect.height - playerHeight + 30);
        
            // Geth length of the points
            float pLength = app.textWidth(String.valueOf(app.getPoints()));
            app.text(app.getPoints(), 385 - pLength, rect.height - playerHeight + 30);
        }
        
        
        // If the bar is high enough, draw the percentage of the player'
        if(rect.height - playerHeight + 100  < rect.y + rect.height)   
        { 
            app.text(String.valueOf((int)percent), 290, rect.height - playerHeight + 70);
            
            // If the percent has 3 digits, place the sign more right.
            if(percent > 99) app.DrawImage("percent_dark", 325, rect.height - playerHeight + 54);
            else  app.DrawImage("percent_dark", 315, rect.height - playerHeight + 54);
        }        
    }
    
    void DrawHighscoreBar(int playerHeight)
    {
        int targetHeight = 0;
     
        // Only if the player has a greater score, than the highscore it is nescessary to calculate the percentage.
        if(playerHeight >= max_bar_height)
        {
            double targetPerc = (double)target.getValue() / ((double)app.getPoints() / 100);
            targetHeight = (int)((100-targetPerc)*48)/10;
        }

        app.fill(255,255,255);

        int x = rect.x + 20;
        int y = rect.height - 480 + targetHeight;
        int w = 150;
        int h = 490 - targetHeight;
        
        app.strokeWeight(2);
        // Targetbar
        for (int i = y; i <= y+h; i++) 
        {
            float inter = app.map(i, y, y+h, 0, 1);
            app.stroke(app.lerpColor(app.color(218,165,32), app.color(255,255,255), inter));
            app.line(x, i, x+w, i);
        }
        app.strokeWeight(0);
        app.fill(0);
        
        int sWidth = 3;
        // Border
        //Left
        app.rect(x-sWidth, y-sWidth+1, sWidth, h+sWidth+1);
        // Top
        app.rect(x-sWidth, y-sWidth, w+sWidth, sWidth);
        // Right
        app.rect(x+w, y-sWidth, sWidth - 1, h+sWidth*2);
        // Bottom
        app.rect(x-sWidth, y+h, w+sWidth+2, sWidth);
        
        for(String key : highscores.keySet())
        {
            // Get the percentage of the score depending of the highest highscore.
            double perc = (double)highscores.get(key) / ((double)target.getValue() / 100);      
            h = (int)(max_bar_height*perc) / 100;              
            h -= (int)(targetHeight*perc)/100;

            app.rect(rect.x+20, rect.height - h - 0, 150, 2);
            
            app.text(key,rect.x+24, rect.height - h + 20 );     
            
            float l = app.textWidth(String.valueOf(highscores.get(key)));    
            
            app.text(highscores.get(key),rect.x+165 - l, rect.height - h + 20);
        }  
        
        app.strokeWeight(2);     
        app.stroke(255);
    }
}
