package com.clubkiwi;

import com.clubkiwi.Character.Kiwi;
import com.clubkiwiserver.Packet.PacketType;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Mathew and trevor(kinda)  on 8/2/2015.
 */
public class CUI implements Runnable
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
        Helper.println(
                " _____  _         _      _   __ _            _ \n" +
                "/  __ \\| |       | |    | | / /(_)          (_)\n" +
                "| /  \\/| | _   _ | |__  | |/ /  _ __      __ _ \n" +
                "| |    | || | | || '_ \\ |    \\ | |\\ \\ /\\ / /| |\n" +
                "| \\__/\\| || |_| || |_) || |\\  \\| | \\ V  V / | |\n" +
                " \\____/|_| \\__,_||_.__/ \\_| \\_/|_|  \\_/\\_/  |_|\n" +
                "                                               ");
        Helper.println("=========================");
        DisplayWelcome();
    }
    public void DisplayWelcome()
    {
        Helper.print("You need to have a registered account to play, type register to make one else type login (type help for instructions on how to play): ");
        String temp = scan.next();
        if(temp.compareToIgnoreCase("register") == 0)
        {
            Register();
        }
        else if(temp.compareToIgnoreCase("login") == 0)
        {
            Login();
        }
        else if(temp.compareToIgnoreCase("help") == 0)
        {
            Help();
        }
        else
        {
            DisplayWelcome();
        }
    }

    private void Help()
    {
        Helper.println("\n" +
                "                                                    __________________\n" +
                "        ||         |||||||||||||||||||             /                  \\\n" +
                "    ____||         |||||||||||||||||||            /      Commands      \\\n" +
                "    \\\\\\\\  []       |||||||||||||||||||           /         feed         \\\n" +
                "     \\____/        |                 |          /          poke         |\n" +
                "     |_____|      _|__ __       __ __|_        /                        |\n" +
                "     |     |     ( |  (_o)-/~\\-(o_)  | )      /                         |\n" +
                "     |     |      (|      (   )      |)      /\\    Your kiwi will get   |\n" +
                "     |     |       |                 |      /  \\   hungry over time you |\n" +
                "     |     |_______|    /       \\    |_________ \\  must feed it or it   |\n" +
                "     |     |        \\     _____     /          \\ \\  will die lol        |\n" +
                "     |     |         \\   (_xD__)   /            \\ \\                     |\n" +
                "     |     |          \\___________/              | \\ __________________/\n" +
                "     |     |    |      |||||||||||               |\n" +
                "     |     /   /        |||||||||       |        |\n" +
                "     |        /          |||||||        |        |\n" +
                "     |       /              {o          |        |\n" +
                "      \\_____/               {o          |        |\n" +
                "          |                 {o          |        |\n" +
                "          |                 {o          |        |");

        DisplayWelcome();
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

    public void MainCharacterScreen()
    {
        Helper.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        Helper.println(ck.getLocalKiwi().toString());
    }

    public void run()
    {
        while(ClubKiwi.running)
        {
            inputLoop();
        }
    }

    public void inputLoop()
    {
        String command = scan.nextLine();

        //Poke command wakes up a sleeping kiwi.
        if (command.compareToIgnoreCase("poke") == 0)
        {
            ck.getLocalKiwi().setSleeping(false);
        }
        else if(command.compareToIgnoreCase("feed") == 0)
        {
            if(ck.getLocalKiwi().isSleeping())
                Helper.println("You cant feed your kiwi when it is sleeping, type poke to wake it.");
            else
                ck.getLocalKiwi().setHunger(100.0);
        }

        //Every command we will update the server on changes even if there were none.
        ck.updateServer();
    }



}
