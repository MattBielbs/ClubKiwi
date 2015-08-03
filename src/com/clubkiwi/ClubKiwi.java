package com.clubkiwi;
import com.clubkiwi.Character.*;
import com.clubkiwiserver.Packet.*;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main game class
 */
public class ClubKiwi
{
    public CUI cui;
    public Connection conn;
    private boolean bLoggedin;
    private int userid;
    private Kiwi localKiwi;

    public ClubKiwi()
    {
        conn = new Connection(this);
        cui = new CUI(this);
        Init();
    }

    public void Init()
    {
        cui.DisplayIntro();
    }

    public void OnPacketReceive(Packet p)
    {
        //debug print
        Helper.println(p.getType() + ": " + Helper.arraytostring(p.getAllData()));

        switch(p.getType())
        {
            case Login_S://This is always an error
                AccountError((Integer) p.getData(0), (String) p.getData(1));
                break;
            case CreateUser_S://This is always an error
                AccountError((Integer) p.getData(0), (String) p.getData(1));
                break;
            case CharacterList_S://You logged in correctly
                LoadCharacter(p);

        }
    }

    private void AccountError(int id, String message)
    {
        Helper.println(message);
        cui.DisplayWelcome();
    }

    private void LoadCharacter(Packet p)
    {
        Helper.println("Logged in!");
        Kiwi k = new Kiwi((String)p.getData(0), (Double)p.getData(1), (Double)p.getData(2), (Double)p.getData(3), (Double)p.getData(4), (Double)p.getData(5), (Double)p.getData(6), (Double)p.getData(7),(Double)p.getData(8),(Double)p.getData(9));
        cui.MainCharacterScreen(k);
    }

}
