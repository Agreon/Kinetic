/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kineticprocessing;

/**
* This window asks the user for his name and shows an countdown afterwards.
*/
public class PreparationWindow extends Window
{
    private String username;
    private short delay;
    private boolean hovering;
    private double countdown;
    private int barY;
    
    
    public PreparationWindow(App app) 
    {
        this.app = app;
        username = "";
        delay = 0;
        hovering = false;
        countdown = -1;
        barY = app.SCREEN_HEIGHT/2 - 55;
        
        if(app.getUsername() != null)
        {
            countdown = 5.99;
            username = app.getUsername();
        }
        
    }

    /**
     * Draw method of this window. 
     * 
     */
    @Override
    void Draw() 
    {
        app.DrawImage("background2", 0, barY+55);
        app.DrawImage("middle_line", 0, barY);
           
        // Name
        app.fill(255,255,255);
        app.textSize(40);
        app.text("Name", 470, 140);
        app.DrawImage("line", 410, 160);
        
        app.textSize(60);
            
        for(int i = 0; i < username.length(); i++)
        {
            char c = username.charAt(i);
            float w = app.textWidth(c);            
                               
            app.text(c, 470 - w/2 +(i*65), 235);
        }
        
        // As long as the user enters his name the input box is displayed.
        if(countdown == -1 ) 
        {           
            app.fill(255,255,255);
            
            for(int i = 3; i > username.length(); i--)
            {
                app.DrawImage("underline", 440 + ((i-1)*70), 235);
               // app.rect(440 + ((i-1)*70),230,50,7);
            }     
            
            if(username.length() == 3) 
            {
                app.textSize(50);
                app.text("Press Return to start!", 260, 580);
            }
            
            app.DrawImage("icon_"+app.getGameMode(), app.SCREEN_WIDTH/2 - 35, barY+20);
        }
        
        // If he pressed Enter the Coutdown is shown.
        else if(!(countdown > -0.5 && countdown < 1)) 
        {
            // Countdown
            app.textSize(60);
            app.fill(0);
            app.text((int)countdown, app.SCREEN_WIDTH/2-22, app.SCREEN_HEIGHT/2 + 18);
        }
        
        else if((countdown > -0.5 && countdown < 1)) app.DrawImage("icon_"+app.getGameMode(), app.SCREEN_WIDTH/2 - 35, barY+20);
    }

    @Override
    int Update() 
    {
        if(delay > 0) delay--;
        if(countdown > 0) countdown -= 0.03;
                
        // if the user did not submit an username yet
        if(countdown == -1)
        {
            // if the namelength is below 3 and the keypress-delay is 0
            if(delay == 0 && username.length() < 3)
            {
                // Check if an key from 'a' to 'z' was pressed.
                for(int i = 97; i < 122; i++)
                {
                    if(app.IsKeyPressed((char)i))
                    {
                        // Append it to the username and set the keypress-delay to 5 frames.
                        username += (char)i;
                        username = username.toUpperCase();
                        delay = 5;
                        break;
                    }
                } 
            }

            // Backspace
            if(app.IsKeyPressed('\b') && username.length() > 0 && delay == 0)
            {
                // Remove last char
                username = username.substring(0,username.length()-1);
                delay = 5;
            }

            // If Return or the submit-button is pressed
            if( (app.IsKeyPressed('\n')  || ( app.collision(510, 240, 49, 49, app.mouseX, app.mouseY) && app.IsLeftMousePressed())) && username.length() == 3 )
            {
                // TODO: Submit username
                // Start Countdown
                app.getLogger().Write("Username: "+username);
                countdown = 5.99;
            }

            // Check if user hovers over submit button.
            hovering = false;
            if(app.collision(510, 240, 49, 49, app.mouseX, app.mouseY))
            {
                hovering = true;
            }
        }
        
        // If countdown is over or return is pressed switch to game screen.
        if((countdown > 0 && countdown < 1) /*|| app.IsKeyPressed('\n')*/)
        {
            barY += 2;
        }
        
        if(barY >= app.SCREEN_HEIGHT - 200)
        {
            app.SetUserName(username);
            return 3;
        }
        
        if(barY > app.SCREEN_HEIGHT/2 - 55 && barY < app.SCREEN_HEIGHT - 200)
        {
            barY += 4;
        }
        
        return 0;
    }
}
