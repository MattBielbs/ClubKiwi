package com.clubkiwi;

import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.Console;
import java.util.Scanner;

/**
 * Created by Mathew on 7/28/2015.
 * To make life easy
 */
public class Helper
{

    public static void print(String text)
    {
        System.out.print(text);
    }

    public static void println(String text)
    {
        System.out.println(text);
    }

    public static String arraytostring(Object[] array)
    {
        String temp = "";
        for(Object o : array)
        {
            try
            {
                temp += (String) o + " ";
            }
            catch(ClassCastException ex)
            {
                if(ex.getMessage().contains("Double"))
                    temp += Double.toString((Double)o) + " ";
                else if(ex.getMessage().contains("Integer"))
                    temp += Integer.toString((Integer)o) + " ";
            }
        }
        return temp;
    }

    //borrowed function
    public static Image makeColorTransparent (Image im, final Color color)
    {
        ImageFilter filter = new RGBImageFilter() {
            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) {
                if ( ( rgb | 0xFF000000 ) == markerRGB ) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                }
                else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
}
