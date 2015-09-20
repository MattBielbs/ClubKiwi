package com.clubkiwi;

import com.clubkiwi.Character.Item;
import com.clubkiwi.Character.ItemType;
import com.clubkiwi.Character.Kiwi;
import com.clubkiwiserver.Packet.PacketType;

import java.util.ArrayList;
import java.util.InputMismatchException;
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
        DisplayIntro();
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
                "     |     |        \\     _____     /          \\ \\  will die LOL        |\n" +
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
        Helper.println("Enter your desried username");
        String username = scan.next();
        Helper.println("Enter your desired password");
        String password = scan.next();
        ck.conn.SendData(PacketType.CreateUser_C, username, password);
    }


    private void Login()
    {
        Helper.println("Enter your username");
        String username = scan.next();
        Helper.println("Enter your password");
        String password = scan.next();
        ck.conn.SendData(PacketType.Login_C, username, password);
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
        try
        {
            String command = scan.nextLine();

            //Poke command wakes up a sleeping kiwi.
            if (command.compareToIgnoreCase("poke") == 0)
            {
                //Every time you wake up your kiwi it gets mad.
                ck.getLocalKiwi().setMood(ck.getLocalKiwi().getMood() - 20.0);
                ck.getLocalKiwi().setSleeping(false);
            }
            else if (command.compareToIgnoreCase("feed") == 0)
            {
                if (ck.getLocalKiwi().isSleeping())
                    Helper.println("You cant feed your kiwi when it is sleeping, type poke to wake it up.");
                else
                {
                    feedPet();
                }
            }
            else
                Helper.println("Command not recognised.");

            //Every command we will update the server on changes even if there were none.
            ck.updateServer();

            //when the server is updated it will sync the changes back to the client.
        }
        catch(Exception ex)
        {
            //do nothing since this will be when we shutdown.
        }
    }

    private void feedPet()
    {
        //foods do different things.
        for(Item item : ClubKiwi.items)
        {
          if(item.getType() == ItemType.Food)
          {
                Helper.println(item.toString());
          }
        }

        Helper.print("Enter the id of the food you wish to give to your kiwi: ");
        try
        {
            int food = scan.nextInt();
            boolean found = false;
            for(Item item : ClubKiwi.items)
            {
                if(item.getType() == ItemType.Food && item.getIndex() == food)
                {
                    ck.getLocalKiwi().giveItem(item);
                    Helper.println("You gave your kiwi " + item.getsName());
                    found = true;
                }
            }

            if(!found)
            {
                Helper.println("The food with that index was not found.");
            }
        }
        catch(InputMismatchException ex)
        {
            Helper.println("You need to enter the number of the food.");
        }

        //ck.getLocalKiwi().setHunger(100.0);
    }



}
