package com.clubkiwi;

import com.clubkiwiserver.Packet.Packet;
import com.clubkiwiserver.Packet.PacketType;
import com.clubkiwiserver.Packet.Serializer;

import java.net.*;

/**
 * Handles connection with the server component
 */
public class Connection implements Runnable
{
    private ClubKiwi ck;
    private DatagramSocket clientSocket;
    private DatagramPacket sendPacket;
    private InetAddress IPAddress;
    private byte[] sendData, receiveData;
    private Serializer s;
    private Integer userid;
    private boolean bLoggedin;

    public Connection(ClubKiwi ck)
    {
        try
        {
            this.ck = ck;
            clientSocket = new DatagramSocket();
            IPAddress = InetAddress.getByName("localhost");
            s = new Serializer();
            receiveData = new byte[1024];

            //Start this class in a new thread
            Thread thread = new Thread(this);
            thread.start();
        }
        catch(Exception ex)
        {
            Helper.println("An exception occurred while creating the connection class.");
        }
    }

    public Integer getUserid()
    {
        return userid;
    }

    public void run()
    {
        while (ClubKiwi.running)
        {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            try
            {
                clientSocket.receive(receivePacket);
            }
            catch(Exception ex)
            {
                Helper.println("Lost connection to server");
                ClubKiwi.running = false;
            }

            Packet p = s.Deserialize(receivePacket.getData());

            if(p != null && p.getAllData().length != 0)
                ck.OnPacketReceive(p);
        }
    }


    public void SendData(PacketType type, Object... objects)
    {
        try
        {
            sendData = s.Serialize(type, objects);
            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5678);
            clientSocket.send(sendPacket);
        }
        catch(Exception ex)
        {
            Helper.println("An error occurred while trying to send a message to the server.");
        }
    }
}
