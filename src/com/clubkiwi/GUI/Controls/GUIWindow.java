package com.clubkiwi.GUI.Controls;

import com.clubkiwi.GUI.GUI;
import com.clubkiwi.GUI.GUIHelper;
import com.clubkiwi.GUI.GUIType;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Mathew on 8/24/2015.
 */
public class GUIWindow extends GUIControl
{
    private Color color;
    private int w, h;
    private ArrayList<GUIControl> children;

    public GUIWindow(String name, int x, int y, int w, int h, Color color)
    {
        super(name, GUIType.Window, x, y);
        this.w = w;
        this.h = h;
        this.color = color;
        this.children = new ArrayList<GUIControl>();
    }

    public void addChild(GUIControl control)
    {
        this.children.add(control);
    }

    @Override
    public void Draw(GL2 gl, TextRenderer render, GUIControl parent)
    {
        //Draw self first
        int xoff = 0, yoff = 0;
        if(parent != null)
        {
            xoff = parent.x;
            yoff = parent.y;
        }

        GUIHelper.DrawRectangle(gl, x + xoff, y + yoff, w, 20, Color.darkGray, 1);
        GUIHelper.DrawRectangle(gl, x + xoff, y + yoff + 20, w, h - 20, color, 1);
        GUIHelper.DrawText(render, name, x + xoff + (w/2) - (name.length()*2), y + yoff + 2, Color.BLACK);

        //Each object is responsible for its children.
        for(GUIControl control : children)
            control.Draw(gl, render, this);
    }
}
