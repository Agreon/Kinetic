/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kineticprocessing;

import java.awt.Rectangle;
import java.util.ArrayList;

class Point2D
{
    public Point2D(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    int x;
    int y;
}

/**
 *
 * @author daniel
 */
public class DiagramModule extends Module
{
    private ArrayList<Point2D> points;
    public DiagramModule(App app, int x, int y, int w, int h)
    {
        this.app = app;
        rect = new Rectangle(x,y,w,h);
        points = new ArrayList<>();
    }
    
    @Override
    void Update()
    {
        // TODO: Get data from serial. At the moment 'r' is just a fixed value.
        int r = 500 - app.getCurrentVoltage()*10;

        // Add a new Point each frame.
        points.add(new Point2D(rect.x+(rect.width/2),r));
        
        // Every point is moved 2 pixels to the left.
        for(Point2D p : points) p.x -= 2;
        
        
        // At the moment you can control r with the 'w' and 's' keys.
       // if(app.IsKeyPressed('w')) r -= 2;
       // if(app.IsKeyPressed('s')) r += 2;
        
    }

    @Override
    void Draw()
    {
        // Background
        app.fill(255,255,255);
        
        // Draw the Grid
        app.fill(128,128,128);
        app.strokeWeight(2);
        for(int i = 0; i*rect.height/10 < rect.height; i++)
        {
            app.line(rect.x, rect.y + i*rect.height/10, rect.x+rect.width, rect.y + i*rect.height/10);
            //app.rect(rect.x, i*rect.height/10, rect.x+rect.width, 2);
        }
        
        // Draw all points.
        app.stroke(255,0,0);
        app.strokeWeight(4);
        for(int i = 0; i < points.size() - 1; i++)
        {
            if(points.get(i).x < rect.x+10 /*|| i % 2 == 0*/) continue;
            
            app.line(points.get(i).x, points.get(i).y, points.get(i+1).x, points.get(i+1).y);
        }        
        app.stroke(0);
    }
        
    public void SaveDiagram()
    {
        app.SetDiagram(points);
        /*
        String line = "";
        
        for(Point2D p : points)
        {
            line += p.x + "," + p.y + ";";
        }
        // Delete last ';'
        line = line.substring(0, line.length()-1);
        
        ArrayList<String> list = new ArrayList<>();
        list.add(line);
        
        app.WriteFile("data/diagram.dat", list);
                */
    }
}
