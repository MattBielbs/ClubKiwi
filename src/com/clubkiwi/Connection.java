package com.clubkiwi;

import com.clubkiwiserver.Packet.Packet;
import com.clubkiwiserver.Packet.PacketType;
import com.clubkiwiserver.Packet.Serializer;

import java.io.IOException;
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
            //clientSocket.setSoTimeout(1000);
            IPAddress = InetAddress.getByName("localhost");
            //clientSocket.connect(IPAddress, 5678);
            s = new Serializer();
            receiveData = new byte[1024];
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
            try
            {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);

                Packet p = s.Deserialize(receivePacket.getData());

                if (p != null && p.getAllData().length != 0)
                    ck.OnPacketReceive(p);

            }
            catch (IOException ex)
            {
                Helper.println("Unable to send packet to server");
            }
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

    public DatagramSocket getClientSocket()
    {
        return clientSocket;
    }
}
