package com.clubkiwi;
import com.clubkiwi.*;
import com.clubkiwi.Character.Kiwi;

import java.util.ArrayList;

/**
 * Main game class
 */
public class ClubKiwi
{
    private DBHelper dbHelper;

    public ClubKiwi()
    {
        dbHelper = new DBHelper();
        Init();
    }

    public void Init()
    {
        Helper.println("Welcome to ClubKiwi");
        Helper.println("=========================");

        Helper.print("Testing login (matypatty,mathew): ");
        Helper.println(dbHelper.Login("matypatty", "mathew") ? "True" : "False");

        Helper.println("Testing Characters:");
        ArrayList<Kiwi> chars = dbHelper.GetCharacters();
        for(Kiwi k : chars)
            Helper.println(k.toString());

        Helper.println("Loading accessories from database:");

    }

}
