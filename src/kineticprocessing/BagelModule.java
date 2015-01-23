/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kineticprocessing;

import java.awt.Rectangle;
import java.math.*;

/**
 *
 * @author daniel
 */
public class BagelModule extends Module
{
    int grad;
    
    BagelModule(App app, int x, int y, int w, int h)
    {
        this.app = app;
        rect = new Rectangle(x,y,w,h);
        
        grad = 270;
    }
    
    @Override
    void Update() 
    {
        
    }

    @Override
    void Draw() 
    {
       
       int x = 535;
       int y = 360;
       int w = 256;
       
       app.fill(255,255,255);

       app.fill(70,130,180);
       
       app.arc(x, y, w, w, app.radians(270), app.radians(grad));
       
       app.fill(255);       
       app.ellipse(x, y, 100, 100);    
       
       app.fill(0);
       app.text(grad, x-100, y-100);
          
       grad += 2;
       
       // Calculate 
       
       //if(grad > 360) grad = 0;
       //if(grad < 0) grad = 360;     
    }
    
}
