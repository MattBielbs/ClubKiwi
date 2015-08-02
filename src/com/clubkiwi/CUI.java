package com.clubkiwi;

import com.clubkiwi.Character.Kiwi;
import com.clubkiwiserver.Packet.PacketType;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Mathew and trevor(kinda)  on 8/2/2015.
 */
public class CUI
{
    private ClubKiwi ck;
    private Scanner scan;

    public CUI(ClubKiwi ck)
    {
        this.ck = ck;
        scan = new Scanner(System.in);
    }

    public void DisplayIntro()
    {
        Helper.println("Welcome to ClubKiwi");
        Helper.println("=========================");
        DisplayWelcome();
    }
    public void DisplayWelcome()
    {
        Helper.print("You need to have a registered account to play, type register to make one else type login: ");
        String temp = scan.next();
        if(temp.compareToIgnoreCase("register") == 0)
        {
            Register();
        }
        else if(temp.compareToIgnoreCase("login") == 0)
        {
            Login();
        }
    }

    private void Register()
    {
        Helper.println("Enter the username and password you wish to use for your account:");
        ck.conn.SendData(PacketType.CreateUser_C, scan.next(), scan.next());
    }

    private void Login()
    {
        Helper.println("Enter your username and password:");
        ck.conn.SendData(PacketType.Login_C, scan.next(), scan.next());
    }

    public void MainCharacterScreen(Kiwi k)
    {
            Helper.println(k.toString());
    }



}
