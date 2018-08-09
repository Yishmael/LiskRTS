package znetwork;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client implements Runnable {
    private String serverName = "localhost";
    private int port = 5555;
    private Socket client;
    private OutputStream outToServer;
    private DataOutputStream out;
    private InputStream inFromServer;
    private DataInputStream in;

    public String sendAndReceive(String message) {
        try {
            client = new Socket(serverName, port);
            client.setSoTimeout(1000);
            System.out.println("Connecting to " + serverName + " on port " + port);

            System.out.println("Connected to " + client.getRemoteSocketAddress());
            outToServer = client.getOutputStream();
            out = new DataOutputStream(outToServer);

            out.writeUTF(message + " " + client.getLocalSocketAddress());
            inFromServer = client.getInputStream();
            in = new DataInputStream(inFromServer);
            // client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return in.readUTF();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        sendAndReceive("Hey from client");
    }

}
