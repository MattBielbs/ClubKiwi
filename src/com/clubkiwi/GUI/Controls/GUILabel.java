package com.clubkiwi.GUI.Controls;

import com.clubkiwi.GUI.GUIHelper;
import com.clubkiwi.GUI.GUIType;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;

/**
 * Created by Mathew on 8/24/2015.
 */
public class GUILabel extends GUIControl
{

    private Color color;

    public GUILabel(String name, int x, int y, Color color)
    {
        super(name, GUIType.Label, x, y);
        this.color = color;
    }

    @Override
    public void Draw(GL2 gl, TextRenderer render, GUIControl parent)
    {
        //Adjust the coordinates dased on parent location.
        if(parent != null)
            GUIHelper.DrawText(render, name, parent.x + x, parent.y + y, color);
        else
            GUIHelper.DrawText(render, name, x, y, color);
    }
}
