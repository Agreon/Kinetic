/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kineticprocessing;

import java.awt.Rectangle;
import java.util.LinkedHashMap;
import java.util.ArrayList;

/**
 * This window shows the main menu.
 */
public class MenuWindow extends Window
{
    int hoverMode;
    ArrayList<LinkedHashMap<String,Integer>> highscores;
    
    Rectangle rects[] = new Rectangle[4]; 
    
    MenuWindow(App app)
    {
        this.app = app;
        hoverMode = -1;
        highscores = app.getHighscores(); 
        
        for(int i = 0; i < 3; i++)
        {
            rects[i] = new Rectangle(90,150+i*150,380,80);
        }
        rects[3] = new Rectangle(100, app.height-80, 100, 49);
    }
    
    // TODO: Animation when hovering about menu points.
    @Override
    void Draw()
    {
        app.textSize(50);

        for(int i = 0; i < 3; i++)
        {
            if(hoverMode == i) app.DrawImage("Menu_Hell_"+(i+1), rects[i].x, rects[i].y, 400, rects[i].height);
            else app.DrawImage("Menu_Dunkel_"+(i+1), rects[i].x, rects[i].y, 400, rects[i].height);
        }
        
        app.fill(255,255,255);
               
        app.textSize(30);
        app.text("Exit", rects[3].x+10, rects[3].y+30);
              
        int i = 0;
        // If the mouse is hovering about a button
        if(hoverMode >= 0 && hoverMode < 3)
        {
            app.DrawImage("Menu_Beschreibung", 490, 50);
            app.DrawImage("Menu_Highscores", 490, 320);
            
            // Select the Highscoredata of the selected gamemode
            for(String key : highscores.get(hoverMode).keySet())
            {               
                app.text(key,620,60*i+500);
                app.text(highscores.get(hoverMode).get(key),800,60*i+500);
                i++;
            }
        }
        
        app.textSize(20);
        if(hoverMode == 0)
        {
            app.text("Druecke so oft du kannst um die",570,180);
            app.text("meisten Punkte zu erzielen!", 570, 220);
        }
        else if(hoverMode == 1)
        {
            app.text("Puste so viel du kannst um die",570,180);
            app.text("meisten Punkte zu erzielen!", 570, 220);
        }
        else if(hoverMode == 2)
        {
            app.text("Drehe die Kurbel so schnell du kannst",550,180);
            app.text("um die meisten Punkte zu erzielen!", 550, 220);
        }
    }
    
    @Override
    int Update()
    {
        // If 'x' is pressed exit the app.
        if(app.IsKeyPressed('x'))
        {
            return -1;
        }
        
        if(app.IsKeyPressed('g'))
        {
            app.SetGameMode(2);
            return 3;
        }
        if(app.IsKeyPressed('s'))
        {
            app.SetGameMode(2);
            app.SetUserName("XXX");
            return 4;
        }
        
        if(app.IsLeftMousePressed()) 
        {
            // First check if mouse is colliding with gamemmode buttons.
            for(int i = 0; i < 3; i++)
            {
                if(app.collision(rects[i].x,rects[i].y,rects[i].width,rects[i].height,app.mouseX,app.mouseY))
                {
                    // Set GameMode
                    app.SetGameMode(i+1);
                    app.getLogger().Write("GameMode: "+app.getGameMode());
                    return 2;
                }
            }
            
            // Then if exit is pressed, exit.
            if(app.collision(rects[3].x,rects[3].y,rects[3].width,rects[3].height,app.mouseX,app.mouseY))
            {
                app.Shutdown();
            }
        }
        
        hoverMode = -1;
        
        // Goes through all rectangles.
        for(int i = 0; i < rects.length; i++)
        {
            // If mouse is colliding with rectangle
            if(app.collision(rects[i].x,rects[i].y,rects[i].width,rects[i].height,app.mouseX,app.mouseY))
            {
                // Set the hovermode to the ambivalent index.
                hoverMode = i;
                break;
            }
        }        
        return 0;
    }
    

}
