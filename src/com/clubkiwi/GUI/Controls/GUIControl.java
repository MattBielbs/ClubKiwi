package com.clubkiwi.GUI.Controls;

import com.clubkiwi.GUI.GUI;
import com.clubkiwi.GUI.GUIType;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;

/**
 * Created by Mathew on 8/24/2015.
 */
public abstract class GUIControl
{
    protected String name;
    protected GUIType type;
    protected int x, y;

    public GUIControl(String name, GUIType type, int x, int y)
    {
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public abstract void Draw(GL2 gl, TextRenderer render, GUIControl parent);
}
