package com.clubkiwi.GUI;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;

/**
 * Created by Mathew on 8/24/2015.
 */
public class GUIHelper
{
    public static void DrawRectangle(GL2 gl, int x, int y, int width, int height, Color color)
    {
        gl.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        gl.glBegin(gl.GL_POLYGON);
        gl.glVertex3f(x, y, 0.0f);
        gl.glVertex3f(x + width, y, 0.0f);
        gl.glVertex3f(x + width, y + height, 0.0f);
        gl.glVertex3f(x, y + height, 0.0f);
        gl.glEnd();
    }

    public static void DrawText(TextRenderer renderer,String text, int x, int y, Color color)
    {
        renderer.beginRendering(800, 600);
        renderer.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        renderer.draw(text, x, y);
        renderer.endRendering();
    }
}
