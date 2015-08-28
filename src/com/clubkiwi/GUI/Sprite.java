package com.clubkiwi.GUI;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mathew on 8/25/2015.
 */
public class Sprite
{
    private Texture texture;

    public Sprite(String path, String type)
    {
        loadtexture(path, type);
    }

    private void loadtexture(String path, String type)
    {
        try
        {
            InputStream stream = getClass().getResourceAsStream(path);
            texture = TextureIO.newTexture(stream, false, type);
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
            System.exit(1);
        }
    }

    public void Draw(GL2 gl, int x, int y)
    {
        texture.enable(gl);
        texture.bind(gl);

        GUIHelper.DrawRectangle(gl, x, y, x + texture.getWidth(), y + texture.getHeight(), Color.white, 1);

    }
}
