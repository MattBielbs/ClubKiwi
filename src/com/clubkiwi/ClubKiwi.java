package com.clubkiwi;
import com.clubkiwi.Character.*;
import com.clubkiwi.GUI.GUI;
import com.clubkiwiserver.Packet.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Main game class
 */
public class ClubKiwi
{
    public GUI gui;
    public Connection conn;
    public CUI cui;

    private Kiwi localKiwi;
    public static boolean running;
    public static List<Item> items;
    private Thread connThread, cuiThread;

    public ClubKiwi()
    {
        running = true;

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Shutdown();
            }
        });

        conn = new Connection(this);
        gui = new GUI(this);
        cui = new CUI(this);
        Init();
    }

    public void Init()
    {
        Helper.println("Starting ClubKiwi please wait...");

        //Items are gonna be created here for now but in future will be sent from the server on initial startup connecion.
        items = new ArrayList<Item>();
        HashMap<String, Double> map1 = new HashMap<String, Double>();
        map1.put("Hunger", 20.0);
        map1.put("Energy", 5.0);
        items.add(new Item(items.size(), "Worms", "| Worms add 20 hunger and 5 energy", 0.0, ItemType.Food, map1));

        HashMap<String, Double> map2 = new HashMap<String, Double>();
        map2.put("Hunger", 5.0);
        map2.put("Energy", 5.0);
        map2.put("Mood", -5.0);
        items.add(new Item(items.size(), "Fruit", "| Keep it fruity you groovy smoothie. Adds 5 hunger and energy", 0.0, ItemType.Food, map2));

        HashMap<String, Double> map3 = new HashMap<String, Double>();
        map3.put("Hunger", -5.0);
        map3.put("Mood", 10.0);
        items.add(new Item(items.size(), "Grubz", "| Doesn't taste very good but makes your kiwi happier. Sacrifices hunger for mood", 0.0, ItemType.Food, map3));

        //Start the connection.
        connThread = new Thread(conn);
        connThread.start();

        //Start the gui.
        cuiThread = new Thread(cui);
        cuiThread.start();
    }

    public void OnPacketReceive(Packet p)
    {
        //debug print
       // Helper.println(p.getType() + ": " + Helper.arraytostring(p.getAllData()));

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
                break;
            case KiwiUpdate_S:
                UpdateKiwi(p);
                break;

        }
    }

    private void UpdateKiwi(Packet p)
    {
        localKiwi.updateKiwi((String) p.getData(0), (Double) p.getData(1), (Double) p.getData(2), (Double) p.getData(3), (Double) p.getData(4), (Double) p.getData(5), (Double) p.getData(6), (Double) p.getData(7), (Double) p.getData(8), (Double) p.getData(9), 0, 0);
        //cui.MainCharacterScreen();
    }

    private void AccountError(int id, String message)
    {
        Helper.println(message);
        //cui.DisplayWelcome();
    }

    private void LoadCharacter(Packet p)
    {
        Helper.println("Logged in!");
        localKiwi = new Kiwi((String)p.getData(0), (Double)p.getData(1), (Double)p.getData(2), (Double)p.getData(3), (Double)p.getData(4), (Double)p.getData(5), (Double)p.getData(6), (Double)p.getData(7),(Double)p.getData(8),(Double)p.getData(9));
        //cui.MainCharacterScreen();
    }

    //So the server can keep track of clients accurately
    public void Shutdown()
    {
        Helper.println("Shutting down...");
        conn.SendData(PacketType.Disconnect, 0);
        ClubKiwi.running = false;
       // conn.getClientSocket().close();
        connThread.interrupt();
        cuiThread.interrupt();
    }

    public Kiwi getLocalKiwi()
    {
        return localKiwi;
    }

    //Updating the server when client changes.
    public void updateServer()
    {
        conn.SendData(PacketType.KiwiUpdate_C, localKiwi.getHealth(), localKiwi.getMoney(), localKiwi.getStrength(), localKiwi.getSpeed(), localKiwi.getFlight(), localKiwi.getSwag(), localKiwi.getHunger(), localKiwi.getMood(), localKiwi.getEnergy());
    }
}
