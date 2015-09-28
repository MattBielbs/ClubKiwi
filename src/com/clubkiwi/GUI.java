package com.clubkiwi;

import com.clubkiwi.Character.Kiwi;
import com.clubkiwiserver.Packet.PacketType;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


/**
 * Created by Mathew on 8/24/2015.
 */
public class GUI extends JFrame implements ActionListener, KeyListener
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
    Room main, login;

    public GUI(ClubKiwi ck)
    {
        super("ClubKiwi");
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        this.ck = ck;
        main = new Room("Main", 2000, 2000, 0, 0, "bg.png");
        login = new Room("Login", 800, 600, 0,0,"login.png");
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
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

    private void ShowLogin()
    {
        //Username
        Label ulabel = new Label("Username:");
        ulabel.setSize(100, 10);
        ulabel.setLocation(200, 220);
        username = new JTextField();
        username.setSize(300, 20);
        username.setLocation(200, 240);

        //Password
        Label plabel = new Label("Password:");
        plabel.setLocation(200, 270);
        plabel.setSize(100,10);
        password = new JPasswordField();
        password.setSize(300, 20);
        password.setLocation(200, 290);
        password.addKeyListener(this);

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

        //Add all
        login.add(ulabel);
        login.add(username);
        login.add(plabel);
        login.add(password);
        login.add(loginb);
        login.add(register);

        SwitchToRoom(login);
    }

    private void SwitchToRoom(Room room)
    {
        if(currentRoom != null)
            remove(currentRoom);

        add(room, BorderLayout.CENTER);
        currentRoom = room;
        camX = room.getStartX();
        camY = room.getStaryY();
        //revalidate();

        //Force refresh
        setSize(799,599);
        setSize(800, 600);
    }

    public void ShowMain()
    {
       // setContentPane(new JLabel(new ImageIcon(ClubKiwi.cldr.getResource("bg.png"))));
        //Add the main gui controls

        //Chatbox
        JPanel footer   =   new JPanel(new BorderLayout());
        chatview = new JList();
       // chatview.setLocation(0, 520);
        chatview.setPreferredSize(new Dimension(800, 80));
        chatview.setListData(chathistory.toArray());
       // chatview.setVisible(true);

        chatbox = new JTextField();
      //  chatbox.setLocation(0, 500);
        chatbox.setPreferredSize(new Dimension(700, 20));
        chatbox.setVisible(true);
        chatbox.addKeyListener(this);

        chatsend = new Button("Send");
     //   chatsend.setLocation(700, 500);
        chatsend.setPreferredSize(new Dimension(100, 20));
        chatsend.addActionListener(this);
        chatsend.setVisible(true);
        footer.setMaximumSize(new Dimension(800, 100));
        footer.add(chatview, BorderLayout.NORTH);
        footer.add(chatbox, BorderLayout.WEST);
        footer.add(chatsend, BorderLayout.EAST);

        SwitchToRoom(main);
        add(footer, BorderLayout.SOUTH);


        //Add local player
        main.add(ck.getLocalKiwi());

        ingame = true;
        revalidate();
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (ingame)
        {
            if (chatbox.isFocusOwner())
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    actionPerformed(new ActionEvent(chatsend, 101, "Send"));
            }
            else
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    chatbox.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_INSERT)
                {
                    for (Kiwi k : ck.players)
                        System.out.println(k);
                }
                if (e.getKeyCode() == KeyEvent.VK_UP)
                {
                    ck.getLocalKiwi().setMovestate(Kiwi.MoveState.Up);
                }
                else if (e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    ck.getLocalKiwi().setMovestate(Kiwi.MoveState.Left);
                }
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    ck.getLocalKiwi().setMovestate(Kiwi.MoveState.Right);
                }
                else if (e.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    ck.getLocalKiwi().setMovestate(Kiwi.MoveState.Down);
                }
            }
        }
        else
        {
            //Login screen password box
            if(e.getKeyCode() == KeyEvent.VK_ENTER && password.isFocusOwner())
                actionPerformed(new ActionEvent(password, 100, "Login"));

        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if(ingame)
            ck.getLocalKiwi().setMovestate(Kiwi.MoveState.None);
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
}
