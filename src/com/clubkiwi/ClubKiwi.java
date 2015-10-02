package com.clubkiwi;
import com.clubkiwi.Character.*;
import com.clubkiwi.Managers.*;
import com.clubkiwiserver.Packet.*;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main game class
 */
public class ClubKiwi
{
    private static Kiwi localKiwi;
    public static boolean running;
    private Thread connThread;

    //Accessible instances
    public static GUI gui;
    public static ConnectionManager connMgr;
    public static InventoryManager invMgr;
    public static InputManager inputMgr;
    public static ResourceManager resMgr;
    public static SoundManager soundMgr;

    //Some arrays.
    public static ArrayList<Kiwi> players;
    public static ArrayList<Item> items;

    public ClubKiwi()
    {
        running = true;

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Shutdown();
            }
        });

        Init();
    }

    private void Init()
    {
        //Start the clock
        long start = System.currentTimeMillis();

        //Create instances
        connMgr = new ConnectionManager(this);
        resMgr = new ResourceManager();
        soundMgr = new SoundManager(this);
        inputMgr = new InputManager(this);
        invMgr = new InventoryManager(this);
        //GUI last.
        gui = new GUI(this);

        //Init lists
        players = new ArrayList<>();
        items = new ArrayList<>();

        initItems();


        //Start the connection.
        connThread = new Thread(connMgr);
        connThread.start();

        Helper.println("ClubKiwi initialized in " + (System.currentTimeMillis() - start) + "ms");
    }

    private void initItems()
    {
        //could be sent from server to add items remotely.
        HashMap<String, Double> map1 = new HashMap<>();
        map1.put("Hunger", 20.0);
        map1.put("Energy", 5.0);
        items.add(new Item(items.size(), "Worms", "Worms add 20 hunger and 5 energy", 0.0, Item.ItemType.Food, map1));

        HashMap<String, Double> map2 = new HashMap<>();
        map2.put("Hunger", 5.0);
        map2.put("Energy", 5.0);
        map2.put("Mood", -5.0);
        items.add(new Item(items.size(), "Fruit", "Keep it fruity you groovy smoothie.", 0.0, Item.ItemType.Food, map2));

        HashMap<String, Double> map3 = new HashMap<>();
        map3.put("Hunger", -5.0);
        map3.put("Mood", 10.0);
        items.add(new Item(items.size(), "Grubz", "Doesn't taste very good but makes you happy.", 0.0, Item.ItemType.Food, map3));

        HashMap<String, Double> map4 = new HashMap<>();
        map4.put("Health", 10.0);
        map4.put("Hunger", 10.0);
        items.add(new Item(items.size(), "BandAid", "Fixes you up", 0.0, Item.ItemType.Food, map4));
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
            case WorldItemAdd:
                gui.getCurrentRoom().addWorldItem(p);
                break;
            case WorldItemRemove:
                gui.getCurrentRoom().removeWorldItem(p);
                break;
            case WorldItemUpdate:
                gui.getCurrentRoom().updateWorldItem(p);
                break;

        }
    }

    private void addChatMessage(int idfrom, String message)
    {
        if(idfrom == -1)
            gui.addChatMessage("Server Message: " + message);
        else
            gui.addChatMessage(getPlayerByID(idfrom).getName() + ": " + message);
    }

    private void playerDisconnect(int id)
    {
        Kiwi k = getPlayerByID(id);
        players.remove(k);
        k.removeKiwi();
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
        //    gui.main.add(temp);

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
            temp.updateKiwiPos((int) p.getData(1), (int) p.getData(2), (int)p.getData(3));
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

        Helper.println("Loading world...");
        gui.StartGameView();
    }

    //So the server can keep track of clients accurately
    private void Shutdown()
    {
        Helper.println("Shutting down...");
        connMgr.SendData(PacketType.Disconnect_C, 0);
        ClubKiwi.running = false;
        connThread.interrupt();
    }

    public static Kiwi getLocalKiwi()
    {
        return localKiwi;
    }

    //Updating the server when client changes.
    public void updateServer()
    {
        connMgr.SendData(PacketType.KiwiUpdate_C, localKiwi.getHealth(), localKiwi.getMoney(), localKiwi.getStrength(), localKiwi.getSpeed(), localKiwi.getFlight(), localKiwi.getSwag(), localKiwi.getHunger(), localKiwi.getMood(), localKiwi.getEnergy());
    }

    @Nullable
    private Kiwi getPlayerByID(int id)
    {
        for(Kiwi k : players)
        {
            if(k.getID() == id)
                return k;
        }

        return null;
    }
}
