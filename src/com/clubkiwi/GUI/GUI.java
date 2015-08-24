package com.clubkiwi.GUI;

import com.clubkiwi.ClubKiwi;
import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Mathew on 8/24/2015.
 */
public class GUI implements Runnable, KeyListener, GLEventListener
{
    private ClubKiwi ck;
    private GLWindow glWindow;
    private Animator animator;
    private TextRenderer renderer;
    public GUI(ClubKiwi ck)
    {
        this.ck = ck;
    }

    @Override
    public void run()
    {
        //Create window
        Display display = NewtFactory.createDisplay(null);
        Screen screen = NewtFactory.createScreen(display, 0);
        GLProfile glProfile = GLProfile.getMaxProgrammable(true);
        GLCapabilities glCapabilities = new GLCapabilities(GLProfile.getMaxFixedFunc(true));
        glWindow = GLWindow.create(screen, glCapabilities);

        glWindow.setTitle("ClubKiwi");
        glWindow.setSize(800, 600);
        glWindow.setPosition(50, 50);
        glWindow.setUndecorated(false);
        glWindow.setAlwaysOnTop(false);
        glWindow.setFullscreen(false);
        glWindow.setPointerVisible(true);
        glWindow.confinePointer(false);
        glWindow.setVisible(true);

        //Set listeners
        glWindow.addGLEventListener(this);
        glWindow.addKeyListener(this);

        animator = new Animator(glWindow);
        animator.start();
    }

    //region OpenGL
    @Override
    public void init(GLAutoDrawable glAutoDrawable)
    {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    /* initialize viewing values */
        gl.glMatrixMode(gl.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0.0f, 800, 600, 0.0f, 0.0f, 1.0f);

        renderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12));
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable)
    {
        this.ck.Shutdown();
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable)
    {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        GUIHelper.DrawRectangle(gl, 10, 10, 100, 100, Color.red);

        GUIHelper.DrawText(renderer, "FUCK IT WORKS LOL", 10, 10, Color.GREEN);


        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height)
    {
        GL gl = glAutoDrawable.getGL();
        gl.glViewport(x, y, width, height);
    }


    //endregion

    //region KeyListener
    @Override
    public void keyPressed(KeyEvent keyEvent)
    {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            this.animator.stop();
            this.glWindow.destroy();
            this.ck.Shutdown();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent)
    {

    }
    //endregion
}
