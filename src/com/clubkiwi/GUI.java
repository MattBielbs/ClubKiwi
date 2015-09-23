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
public class GUI extends JFrame implements ActionListener, KeyListener
{
    private final ClubKiwi ck;
    private JTextField username, chatbox;
    private JPasswordField password;
    private JList chatview;
    private ArrayList<String> chathistory = new ArrayList<>();
    private boolean ingame = false;

    public GUI(ClubKiwi ck)
    {
        super("ClubKiwi");
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        this.ck = ck;
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
                ClubKiwi.conn.SendData(PacketType.CreateUser_C, username.getText(), String.valueOf(password.getPassword()));
                break;
            case "Send":
                ClubKiwi.conn.SendData(PacketType.Chat_C, chatbox.getText());
                chatbox.setText("");
                break;
        }
    }

    private void ShowLogin()
    {
        setContentPane(new JLabel(new ImageIcon(ClubKiwi.cldr.getResource("login.png"))));

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

        //Login button
        Button login = new Button("Login");
        login.setSize(100, 20);
        login.setLocation(200, 320);
        login.addActionListener(this);

        //Register button
        Button register = new Button("Register");
        register.setSize(100, 20);
        register.setLocation(320, 320);
        register.addActionListener(this);

        //Add all
        add(ulabel);
        add(username);
        add(plabel);
        add(password);
        add(login);
        add(register);

        //Force refresh
        setSize(799,599);
        setSize(800,600);
    }

    public void ShowMain()
    {
        setContentPane(new JLabel(new ImageIcon(ClubKiwi.cldr.getResource("main.png"))));

        //Add the main gui controls

        //Chatbox
        chatview = new JList();
        chatview.setLocation(0, 520);
        chatview.setSize(800, 80);
        chatview.setListData(chathistory.toArray());
       // chatview.setVisible(true);

        chatbox = new JTextField();
        chatbox.setLocation(0, 500);
        chatbox.setSize(700, 20);
        chatbox.setVisible(true);

        Button chatsend = new Button("Send");
        chatsend.setLocation(700, 500);
        chatsend.setSize(100, 20);
        chatsend.addActionListener(this);
        chatsend.setVisible(true);

        add(chatview);
        add(chatbox);
        add(chatsend);

        //Add local player
        add(ck.getLocalKiwi());

        //Force refresh
        setSize(799, 599);
        setSize(800, 600);
        setVisible(true);
        revalidate();
        ingame = true;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(ingame)
        {
            if(e.getKeyCode() == KeyEvent.VK_INSERT)
            {
                for(Kiwi k : ck.players)
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

    @Override
    public void keyReleased(KeyEvent e)
    {
        ck.getLocalKiwi().setMovestate(Kiwi.MoveState.None);
    }

    public void addChatMessage(String message)
    {
        chathistory.add(0, message);
        chatview.setListData(chathistory.toArray());
        chatview.ensureIndexIsVisible(chathistory.size());
    }


}
