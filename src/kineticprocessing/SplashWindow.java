/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kineticprocessing;

/**
 *
 * @author daniel
 */
public class SplashWindow extends Window
{
    private int counter;
    private int alpha;
    private int mode;
    
    SplashWindow(App app)
    {
        this.app = app;
        counter = 0;
        alpha = 0;
        mode = 0;
    }
    
    @Override
    void Draw() 
    {
        app.tint(255, 255, 255, alpha);
        app.DrawImage("splashscreen", 0, 0);
        app.tint(255);
    }

    @Override
    int Update()
    {   
        if(mode == 0)
        {
            if(counter < 50) counter++;
            if(alpha < 255) alpha += 5;
        }
        else if(mode == 1)
        {
            if(counter > 0) counter--;
            else
            {
                mode = 2;
            }
        }
        else
        {
            counter++;
            alpha -= 5;
        }
        
        if(mode == 0 && counter == 50) mode = 1;  
        if(mode == 2 && counter == 65) return 1;
        
        return 0;
    }
    
}
