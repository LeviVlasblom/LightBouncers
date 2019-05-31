package lightbouncers.net.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client
{
    private ITCPClientReceiver receiver;
    private String host;
    private int port;
    private Socket clientSocket;
    private Thread listenerThread;

    private boolean isConnected;

    public Client(String host, int port,  ITCPClientReceiver receiver)
    {
        this.host = host;
        this.port = port;
        this.isConnected = false;
        this.receiver = receiver;
        this.listenerThread = new Thread("ClientSocketListener"){
            public void run()
            {
                while(isConnected)
                {
                    listen();
                }
            }
        };
    }

    public boolean connect()
    {
        try
        {
            if(!this.isConnected)
            {
                this.clientSocket = new Socket(this.host, this.port);
                this.isConnected = true;
                this.listenerThread.start();
                return true;
            }
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            System.out.println("Could not connect to server!");
        }
        return this.isConnected;
    }

    public void disconnect()
    {
        try
        {
            if(this.isConnected)
            {
                System.out.println("Disconnected from server!");
                this.isConnected = false;
                if(this.listenerThread != null)
                {
                    this.listenerThread.stop();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void send(String data)
    {
        try
        {
            if(this.isConnected)
            {
                DataOutputStream clientOutput = new DataOutputStream(this.clientSocket.getOutputStream());
                if(this.clientSocket != null)
                {
                    clientOutput.writeBytes(data + "\n");
                    System.out.println("Client send: " + data);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("No connection found!");
        }
    }

    public void receive(String data)
    {
        if(!data.isEmpty())
        {
            if(this.receiver != null)
            {
                this.receiver.receive(data);
            }
            System.out.println("Client received: " + data);
        }
    }

    public void listen()
    {
        try
        {
            BufferedReader clientInput = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            DataOutputStream clientOutput = new DataOutputStream(this.clientSocket.getOutputStream());

            String serverInput = clientInput.readLine();

            if(serverInput != null)
            {
                receive(serverInput);
            }
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            disconnect();
        }
    }
}
