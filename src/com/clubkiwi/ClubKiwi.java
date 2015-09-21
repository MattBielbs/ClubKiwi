package com.clubkiwi;
import com.clubkiwi.Character.*;
import com.clubkiwiserver.Client;
import com.clubkiwiserver.Packet.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Main game class
 */
public class ClubKiwi
{
    public static GUI gui;
    public static Connection conn;

    private Kiwi localKiwi;
    public static boolean running;
    public static List<Item> items;
    private Thread connThread, cuiThread;
    public ArrayList<Kiwi> players;
    public static ClassLoader cldr;

    public ClubKiwi()
    {
        cldr = this.getClass().getClassLoader();
        running = true;

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Shutdown();
            }
        });

        conn = new Connection(this);
        gui = new GUI(this);
        Init();
    }

    public void Init()
    {
        players = new ArrayList<Kiwi>();

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
    }

    public void OnPacketReceive(Packet p)
    {
        //debug print
        //Helper.println(p.getType() + ": " + Helper.arraytostring(p.getAllData()));

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
            case Disconnect_S://Server is telling you about a disconnect
                playerDisconnect((int)p.getData(0));
                break;
            case OtherPlayer_S://Server is telling you about another player
                otherPlayerInfo(p);
                break;
            case KiwiPos_S://Server is tewlling you about another players pos
                otherPlayerPos(p);
                break;
            case Chat_S:
                addChatMessage((int)p.getData(0), (String)p.getData(1));
                break;

        }
    }

    private void addChatMessage(int idfrom, String message)
    {
        gui.addChatMessage(getPlayerByID(idfrom).getName() + ": " + message);
    }

    private void playerDisconnect(int id)
    {
        Kiwi k = getPlayerByID(id);
        players.remove(k);
        gui.remove(k);
        gui.repaint();
    }

    private void otherPlayerInfo(Packet p)
    {
        //Check if client already exists.
        Kiwi temp = getPlayerByID((int) p.getData(0));
        if (temp == null)
        {
            //create player
            temp = new Kiwi((String) p.getData(1), (Double) p.getData(2), (Double) p.getData(3), (Double) p.getData(4), (Double) p.getData(5), (Double) p.getData(6), (Double) p.getData(7), (Double) p.getData(8), (Double) p.getData(9), (Double) p.getData(10));
            temp.setID((int) p.getData(0));
            players.add(temp);
            gui.add(temp);
        }
        else
        {
            //update player
            temp.updateKiwi((String) p.getData(1), (Double) p.getData(2), (Double) p.getData(3), (Double) p.getData(4), (Double) p.getData(5), (Double) p.getData(6), (Double) p.getData(7), (Double) p.getData(8), (Double) p.getData(9), (Double) p.getData(10));
        }
    }

    private void otherPlayerPos(Packet p)
    {
        Kiwi temp = getPlayerByID((int) p.getData(0));

        //if this is null then player hasnt connected and thats gonna be a problem
        if (temp != null)
            temp.updateKiwiPos((int) p.getData(1), (int) p.getData(2));
    }

    private void UpdateKiwi(Packet p)
    {
        localKiwi.updateKiwi((String) p.getData(0), (Double) p.getData(1), (Double) p.getData(2), (Double) p.getData(3), (Double) p.getData(4), (Double) p.getData(5), (Double) p.getData(6), (Double) p.getData(7), (Double) p.getData(8), (Double) p.getData(9));
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
        localKiwi.setID((int)p.getData(10));
        players.add(localKiwi);

        Thread temp = new Thread(localKiwi);
        temp.start();

        Helper.println("Starting ClubKiwi please wait...");
        gui.ShowMain();
    }

    //So the server can keep track of clients accurately
    public void Shutdown()
    {
        Helper.println("Shutting down...");
        conn.SendData(PacketType.Disconnect_C, 0);
        ClubKiwi.running = false;
       // conn.getClientSocket().close();
        connThread.interrupt();
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

    public Kiwi getPlayerByID(int id)
    {
        for(Kiwi k : players)
        {
            if(k.getID() == id)
                return k;
        }

        return null;
    }
}
