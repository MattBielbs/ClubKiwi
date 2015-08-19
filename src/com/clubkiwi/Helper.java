package com.clubkiwi;

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
}
