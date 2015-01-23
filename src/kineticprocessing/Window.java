/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kineticprocessing;

/**
 * This abstract class is used by all Windows this Application consists of.
 */
public abstract class Window
{
    protected App app;
    abstract void Draw();
    abstract int Update();
}
