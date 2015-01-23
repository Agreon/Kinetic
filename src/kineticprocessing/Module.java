/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kineticprocessing;

import java.awt.Rectangle;

/**
 * Modules can be used by windows to define different areas on the screen.
 */
public abstract class Module 
{
    protected Rectangle rect;
    protected App app;
    abstract void Update();
    abstract void Draw();
}
