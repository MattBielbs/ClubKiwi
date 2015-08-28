package com.clubkiwi.GUI;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;

/**
 * Created by Mathew on 8/24/2015.
 */
public class GUIHelper
{
    public static void DrawRectangle(GL2 gl, int x, int y, int width, int height, Color color, int border)
    {
        gl.glLineWidth(border);
        float r=(1.0f/255)*color.getRed();
        float g=(1.0f/255)*color.getGreen();
        float b=(1.0f/255)*color.getBlue();
        gl.glColor3f(r, g, b);
        gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_FILL);
        gl.glBegin(gl.GL_POLYGON);
        gl.glVertex3f(x, y, 0.0f);
        gl.glVertex3f(x + width, y, 0.0f);
        gl.glVertex3f(x + width, y + height, 0.0f);
        gl.glVertex3f(x, y + height, 0.0f);
        gl.glEnd();

        gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_LINE);
        gl.glBegin(gl.GL_POLYGON);
        gl.glVertex3f(x, y, 0.0f);
        gl.glVertex3f(x + width, y, 0.0f);
        gl.glVertex3f(x + width, y + height, 0.0f);
        gl.glVertex3f(x, y + height, 0.0f);
        gl.glEnd();

        gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_FILL);
    }

    public static void DrawText(TextRenderer renderer,String text, int x, int y, Color color)
    {
        renderer.beginRendering(800, 600);
        renderer.setColor(color.getRed(),color.getGreen(),color.getBlue(),1.0f);
        renderer.draw(text, x, (600 - y) - renderer.getFont().getSize());
        renderer.endRendering();
    }
}
