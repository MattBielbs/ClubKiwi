package com.clubkiwi;
import com.clubkiwi.*;
import com.clubkiwi.Character.Accessory;
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
       // byte[] data = Packer.Serialize((byte)ServerPacket.UpdatePlayer, c.CI.ID, c.CI.PosX, c.CI.PosY, c.CI.Rotation);
     //   cc.Send(data);

        Helper.println("Welcome to ClubKiwi");
        Helper.println("=========================");

        Helper.print("Testing login (matypatty, mathew): ");
        Helper.println(dbHelper.Login("matypatty", "mathew") ? "True" : "False");
        //Helper.print("Testing login (Purple_C0wz, dank): ");
        //Helper.println(dbHelper.Login("Purple_C0wz", "dank") ? "True" : "False");

        Helper.println("Testing Characters:");
        ArrayList<Kiwi> chars = dbHelper.GetCharacters();
        for(Kiwi k : chars)
            Helper.println(k.toString());

        Helper.println("Loading accessories from database:");
        ArrayList<Accessory> accs = dbHelper.GetAccessories();
        for(Accessory a : accs)
            Helper.println(a.toString());


    }

}
