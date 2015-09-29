package com.clubkiwi;

import com.clubkiwi.Character.Kiwi;
import com.clubkiwiserver.Packet.PacketType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


/**
 * Created by Mathew on 8/24/2015.
 */
public class GUI extends JFrame implements ActionListener
{
    private final ClubKiwi ck;
    private JTextField username, chatbox;
    private JPasswordField password;
    private JList chatview;
    private Button chatsend;
    private ArrayList<String> chathistory = new ArrayList<>();
    private boolean ingame = false;


    //Camera and map shit
    //camera size is 800x500
    private int camX, camY;
    private Room currentRoom;

    //Rooms
    public Room main, login, room2;
    public ArrayList<Room> rooms = new ArrayList<>();

    public GUI(ClubKiwi ck)
    {
        super("ClubKiwi | Pretty much the best game (not a clubpenguin ripoff)");
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        this.ck = ck;
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        loadRooms();
        ShowLogin();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "Login":
                    ClubKiwi.conn.SendData(PacketType.Login_C, username.getText(), String.valueOf(password.getPassword()));
                break;
            case "Register":
                if(username.getText().isEmpty() || String.valueOf(password.getPassword()).isEmpty())
                    JOptionPane.showMessageDialog(null, "You must enter a username and password.", "Error", JOptionPane.WARNING_MESSAGE);
                else if (username.getText().length() >= 12)
                    JOptionPane.showMessageDialog(null, "Your username must be no more than 12 characters", "Error", JOptionPane.WARNING_MESSAGE);
                else
                    ClubKiwi.conn.SendData(PacketType.CreateUser_C, username.getText(), String.valueOf(password.getPassword()));
                break;
            case "Send":
                ClubKiwi.conn.SendData(PacketType.Chat_C, chatbox.getText());
                chatbox.setText("");
                requestFocus();
                break;
        }
    }

    private void loadRooms()
    {
        main = new Room(0, "Main", 2000, 2000, 0, 0, "bg.png");
        login = new Room(1, "Login", 800, 600, 0,0,"login.png");
        room2 = new Room(2, "Room2", 1024, 768, 0, 0, "bg2.png");

        rooms.add(main);
        rooms.add(login);
        rooms.add(room2);
    }

    private void ShowLogin()
    {
        //Username
        JLabel ulabel = new JLabel("Username:");
        ulabel.setSize(100, 10);
        ulabel.setLocation(200, 220);
        username = new JTextField();
        username.setSize(300, 20);
        username.setLocation(200, 240);

        //Password
        JLabel plabel = new JLabel("Password:");
        plabel.setLocation(200, 270);
        plabel.setSize(100,10);
        password = new JPasswordField();
        password.setSize(300, 20);
        password.setLocation(200, 290);

        //Login button
        Button loginb = new Button("Login");
        loginb.setSize(100, 20);
        loginb.setLocation(200, 320);
        loginb.addActionListener(this);

        //Register button
        Button register = new Button("Register");
        register.setSize(100, 20);
        register.setLocation(320, 320);
        register.addActionListener(this);

        //Little text
        JLabel text = new JLabel("Developed By Mathew Bielby, Trevor Hastelow and Jung Wook Lee. Graphics by Lisa Crabtree");
        text.setLocation(110,550);
        text.setSize(600, 20);
        text.setBackground(Color.WHITE);

        //Add all
        login.add(ulabel);
        login.add(username);
        login.add(plabel);
        login.add(password);
        login.add(loginb);
        login.add(register);
        login.add(text);

        SwitchToRoom(login);
        username.requestFocus();
    }

    public void SwitchToRoom(Room room)
    {
        if(currentRoom != null)
            remove(currentRoom);

        add(room, BorderLayout.CENTER);
        currentRoom = room;
        camX = room.getStartX();
        camY = room.getStaryY();
        //revalidate();

        //swap the localkiwi
        if(ck.getLocalKiwi() != null)
            ck.getLocalKiwi().swaproom(currentRoom);

        //add the inventory
        boolean hasinv = false;
        for(Component c : room.getComponents())
        {
            if(c == ck.inv)
                hasinv = true;
        }

        if(!hasinv)
            room.add(ck.inv, 0);

        //Force refresh
        setSize(799, 599);
        setSize(800, 600);
    }

    public void StartGameView()
    {
        //Chatbox
        JPanel footer = new JPanel(new BorderLayout());
        chatview = new JList();
        chatview.setPreferredSize(new Dimension(800, 80));
        chatview.setListData(chathistory.toArray());

        chatbox = new JTextField();
        chatbox.setPreferredSize(new Dimension(700, 20));
        chatbox.setVisible(true);

        chatsend = new Button("Send");
        chatsend.setPreferredSize(new Dimension(100, 20));
        chatsend.addActionListener(this);
        chatsend.setVisible(true);
        footer.setMaximumSize(new Dimension(800, 100));
        footer.add(chatview, BorderLayout.NORTH);
        footer.add(chatbox, BorderLayout.WEST);
        footer.add(chatsend, BorderLayout.EAST);

        add(footer, BorderLayout.SOUTH);

        SwitchToRoom(main);

        //Only add player to start room it will move itself with switchtoroom.
       // main.add(ck.getLocalKiwi());
        ingame = true;
        revalidate();
    }

    public void ShowMain()
    {
        SwitchToRoom(main);

        //Add local player
        main.add(ck.getLocalKiwi(), 1);




    }

    public void addChatMessage(String message)
    {
        chathistory.add(0, message);
        chatview.setListData(chathistory.toArray());
        chatview.ensureIndexIsVisible(chathistory.size());
    }

    public int getCamX()
    {
        return camX;
    }

    public int getCamY()
    {
        return camY;
    }

    public void setCamX(int camX)
    {
        this.camX = camX;
    }

    public void setCamY(int camY)
    {
        this.camY = camY;
    }

    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    public boolean isIngame()
    {
        return ingame;
    }

    public JPasswordField getPassword()
    {
        return password;
    }

    public JTextField getChatbox()
    {
        return chatbox;
    }

    public Button getChatsend()
    {
        return chatsend;
    }
}
