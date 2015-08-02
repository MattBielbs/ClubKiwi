package com.clubkiwi;
import java.io.*;
import java.net.*;
import com.clubkiwiserver.Packet.*;

public class Main {

    static Serializer s;
    public static void main(String[] args) throws Exception
    {
        s = new Serializer();

        //ClubKiwi ck = new ClubKiwi();

        BufferedReader inFromUser =          new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        sendData = s.Serialize(PacketType.Test, "if this actually works wtf im god", "trevor pls");
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        clientSocket.send(sendPacket);


        clientSocket.close();

    }
}
