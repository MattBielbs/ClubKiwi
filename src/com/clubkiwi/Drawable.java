package com.clubkiwi;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;

/**
 * Created by Mathew on 8/25/2015.
 */
public abstract class Drawable
{
    protected int x, y;
    protected boolean visible;

    public Drawable(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.visible = true;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public void Draw(GL2 gl, TextRenderer render)
    {
        if(this.visible)
            doDraw(gl, render);
    }

    protected abstract void doDraw(GL2 gl, TextRenderer render);
}
