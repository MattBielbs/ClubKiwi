package com.clubkiwi;

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
            temp += (String)o + " ";
        }
        return temp;
    }

}
