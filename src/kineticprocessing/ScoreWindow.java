/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kineticprocessing;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * This window will show the score and informations.
 */
public class ScoreWindow extends Window
{
    ArrayList<Module> modules;
    private int mode;
    private int bar_height;
    
    // Menu
    Rectangle rects[];
    String menupoints[];
    int hoverMode;
    int currentHighscore;
    String currentHighscoreName;
    
    
    // Highscorebagel
    LinkedHashMap<String,Integer> highscores;
    double targetPercentage;
    double currentPercentage;
    
    // Diagram
    ArrayList<Point2D> diagram;
    
    
    public ScoreWindow(App app)
    {
        this.app = app;        
        this.modules = new ArrayList<>();
        modules.add(new BagelModule(app,400,400,400,400));
        
        mode = 0;
        hoverMode = -1;
        bar_height = app.SCREEN_HEIGHT-200;
        
        rects = new Rectangle[3];
        rects[0] = new Rectangle(100, 550, 200, 40);      
        rects[1] = new Rectangle(400, 550, 290, 40);
        rects[2] = new Rectangle(800, 550, 150, 40);

        menupoints = new String[3];
        menupoints[0] = "Hauptmenu";
        menupoints[1] = "Nochmal Spielen";
        menupoints[2] = "Beenden";
        
        highscores = app.getHighscores().get(app.getGameMode()-1);
        
        // Read highest Highscore
        for(String key : highscores.keySet())
        {        
           currentHighscore = highscores.get(key);
           currentHighscoreName = key;
           break;
        } 
        targetPercentage = ((double)app.getPoints() / (double)currentHighscore) * 100;
        currentPercentage = 0;

        System.out.println(app.getPoints());
        SaveHighScore();
        
        diagram = app.getDiagram();
        
        app.fill(255,255,255);
    }
    @Override
    void Draw() 
    {
        app.DrawImage("middle_line", 0, bar_height);
        if(mode == 0)
        {
            app.DrawImage("icon_"+app.getGameMode(), app.SCREEN_WIDTH/2 - 35, bar_height+20);
        }
        if(mode == 1)
        {
            app.textSize(60);
           // app.text("Score",450,100);
            
            // Draw Score
            app.fill(255);
            /*app.DrawImage("line", 100, 175);
            
            app.textSize(40);
            app.text("Score", 160, 155);
            app.textSize(20);
            app.text("Punkte",130, 220);
            app.text("Highscore", 100, 250);
            app.text(app.getPoints(), 250, 220);
            app.text(currentHighscore, 250, 250);
            */
            app.DrawImage("line", 90, 155);
            
            app.textSize(50);
            app.text("Score", 150, 135);
            app.textSize(30);
            app.text("Punkte",105, 200);
            app.text("Highscore", 60, 240);
            app.text(app.getPoints(), 260, 200);
            app.text(currentHighscore, 260, 240);
            
            // Menu
            app.textSize(30);
            for(int i = 0; i < 3; i++)
            {
                if(hoverMode == i) app.fill(255);
                else app.fill(191,191,191);
                
                app.text(menupoints[i],rects[i].x, rects[i].y+30);
            }  
            app.fill(255);
            
            DrawBagel();
            DrawDiagram();
           // for(Module module : modules) module.Draw();
        }
       
    }

    @Override
    int Update() 
    {
        for(Module module : modules) module.Update();
                
        if(bar_height > app.SCREEN_HEIGHT/2 - 55)
        {
            bar_height -= 4;
        }
        else
        {
            mode = 1;
            if(currentPercentage < targetPercentage) currentPercentage++;
        }
        
        
        // Menu
        hoverMode = -1;
        // Goes through all rectangles.
        for(int i = 0; i < rects.length; i++)
        {
            // If mouse is colliding with rectangle
            if(app.collision(rects[i].x,rects[i].y,rects[i].width,rects[i].height,app.mouseX,app.mouseY))
            {
                if(app.IsLeftMousePressed())
                {
                    if(i == 0)
                    {
                        app.SetUserName(null);
                        app.SetPoints(0);
                        app.SetGameMode(-1);
                        return 1;
                    }
                    else if(i == 1)
                    {
                        app.SetPoints(0);
                        return 2;
                    }
                    else if(i == 2) return -1;
                }
                else
                {
                    // Set the hovermode to the ambivalent index.
                    hoverMode = i;
                    break;
                }
            }
        }  
        
        return 0;
    }
    
    void DrawBagel()
    {       
       int degrees = (int) (270 + (currentPercentage * 3.6));
       
       // TODO: maybe highscore points.
       app.textSize(40);
     //  app.strokeWeight(2);
     //  app.stroke(255);
      // app.text(app.getUsername(), 500, 210);
       
        for(int i = 0; i < app.getUsername().length(); i++)
        {
            char c = app.getUsername().charAt(i);
            float w = app.textWidth(c);            
                               
            app.text(c, 500 - w/2 +(i*35), 210);
        }
       
       app.textSize(30);
       
       app.line(535,240,535,320);
       //app.stroke(0);
       
       app.fill(70,130,180);
       app.arc(535, 360, 256, 256, app.radians(270), app.radians(degrees));
       
       // TODO: Maybe add a line at the current position.
       
       //app.stroke(255,0,0);
       app.fill(255);       
       app.ellipse(535, 360, 100, 100); 
       
       app.stroke(0);
       app.fill(0);
       //app.text((int) currentPercentage, 510, 370);
       if(currentPercentage > 99)  app.text((int) currentPercentage, 500, 370);
       else  app.text((int) currentPercentage, 510, 370);
       app.DrawImage("percent_dark", 540, 350);
    }
    
    void DrawDiagram()
    {
        double originLength = diagram.size() * 2;
        double originHeight = 500;
        double diagramLength = 240;
        double diagramHeight = 250;
        int relationW = (int) (originLength / diagramLength);        
        int relationH = (int) (originHeight / diagramHeight); 
                        
        int x = 720;
        int y = 50;
        int w = 300;
        int h = 300;
        
        app.fill(128,128,128);
        app.strokeWeight(2);
        app.stroke(255);
        for(int i = 0; i*h/10 < h; i++)
        {
            app.line(x, y + i*h/10, x+w, y + i*h/10);
        }

        app.stroke(255,0,0);
        app.strokeWeight(4);
        for(int i = 0; i < diagram.size() - 1; i++)
        {
            app.line(diagram.get(i).x / relationW + 880, diagram.get(i).y / relationH +30, diagram.get(i+1).x  / relationW + 880, diagram.get(i+1).y / relationH +30);
        }
        app.stroke(0);
    }
    
    /**
     * Saves the new Highscore, if there is one. 
     */
    void SaveHighScore()
    {
        LinkedHashMap<String,Integer> highscores = app.getHighscores().get(app.getGameMode()-1);

        int place = -1;
        ArrayList<Integer> values = new ArrayList<>();
        ArrayList<String> werte = new ArrayList<>();
        String finalScore = "";
        
        // Read all Highscores
        for(String key : highscores.keySet())
        {        
            values.add(highscores.get(key));
            werte.add(key+":"+highscores.get(key));
        }  
        
        // Check the place of the new highscore.
        for(int i = 0; i < values.size(); i++)
        {
            if(app.getPoints() > values.get(i))
            {
                place = i;
                break;
            }
        }
        
        // if no highscore was reached return.
        if(place == -1) return;
        
        // Add the highscore at its place and remove the 4.
        werte.add(place, app.getUsername() + ":" + app.getPoints());
        werte.remove(werte.size()-1);
        
        // Connect all values to one string.
        for(int i = 0; i < werte.size(); i++) finalScore += werte.get(i) + ";";
       
        // Delete the last ';'.
        finalScore = finalScore.substring(0, finalScore.length()-1);
            
        // Replace the line of the gamemode.
        ArrayList<String> lines = app.readFile(app.HIGHSCORE_FILE);
        lines.set(app.getGameMode()-1, finalScore );
        
        // Write all lines to the highscore file.
        app.WriteFile("data/scores.khsf", lines);
    }
}
