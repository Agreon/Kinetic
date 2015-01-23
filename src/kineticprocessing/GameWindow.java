/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kineticprocessing;
 
/**
 * This window shows the GameScreen.
 * It includes different Modules to show informations about the score.
 */
import java.util.ArrayList;
import java.util.Date;
public class GameWindow extends Window
{
    private ArrayList<Module> modules;
    private Date startTime;
    private Date currentTime;
    private int seconds;
    private final int gameTime = 30;
    
    public GameWindow(App app, ArrayList<Module> modules)
    {
        this.app = app;
        this.modules = modules;
        startTime = app.getTime();
        currentTime = app.getTime();
    }
    
    @Override
    void Draw() 
    {
        app.DrawImage("background2", 0, app.SCREEN_HEIGHT-145);
        app.DrawImage("middle_line", 0, app.SCREEN_HEIGHT-200);
        app.DrawImage("icon_"+app.getGameMode(), app.SCREEN_WIDTH/2 - 35, app.SCREEN_HEIGHT-200+20);

        // Draw every module.
        for(Module module : modules) module.Draw();
             
        app.textSize(80);
        
        // When the time hits 5 Seconds, draw the time red.
        if(gameTime - seconds < 6) app.fill(255,0,0);
        else app.fill(255,255,255);
                
        // Draw Time
        if(gameTime - seconds > 9) app.text(gameTime - seconds, 580 - app.textWidth(String.valueOf(gameTime-seconds)), 350);
        else app.text(gameTime - seconds, 560 - app.textWidth(String.valueOf(gameTime-seconds)), 350);
        
        app.fill(255);
        // Draw points
        if(app.getPoints() > 999) app.text(app.getPoints(), 620 - app.textWidth(String.valueOf(app.getPoints())), 460);
        else app.text(app.getPoints(), 600 - app.textWidth(String.valueOf(app.getPoints())), 460);
    }

    @Override
    int Update() 
    {
        app.SetPoints(app.getPoints()+app.getCurrentVoltage()/3);
        // Update every module.
        for(Module module : modules) module.Update();
        currentTime = app.getTime();
        
        // Calculate how long the game is running already.
        long milliDiff = currentTime.getTime() - startTime.getTime();
        seconds = (int) (milliDiff/1000);
        
        // If the gametime is over, switch to score window.
        if(seconds > gameTime || app.IsKeyPressed('\n'))
        {            
            // Save Diagram
            DiagramModule m = (DiagramModule) modules.get(1);
            // Be sure, that module 2 is really a DiagramModule.
            if(m instanceof DiagramModule)
            { 
                m.SaveDiagram();
            }
            else
            {
                app.getLogger().Write("[ERROR] Could not save diagram!");
                app.Shutdown();
            }

            return 4;
        }
        
        return 0;   
    }
}
