package lightbouncers.net.client;

import lightbouncers.net.PlayerObject;
import lightbouncers.objects.pawns.characters.PlayerCharacter;
import lightbouncers.world.World;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;

public class Client
{
    private ITCPClientReceiver receiver;
    private String host;
    private int port;
    private Socket clientSocket;
    private Thread listenerThread;

    private boolean readWriteObjectMode;
    private boolean gameIsInProgress;
    private boolean isConnected;

    private World world;

    public Client(String host, int port, ITCPClientReceiver receiver)
    {
        this.host = host;
        this.port = port;
        this.readWriteObjectMode = false;
        this.gameIsInProgress = false;
        this.isConnected = false;
        this.receiver = receiver;
        this.listenerThread = new Thread("ClientSocketListener"){
            public void run()
            {
                while(isConnected)
                {
                    listen();

                    if(gameIsInProgress)
                    {
                        //Send player data
                        if(world != null)
                        {
                            PlayerCharacter playerCharacter = world.getPlayer();
                            PlayerObject playerObject = new PlayerObject(playerCharacter.getWorldPosition(), playerCharacter.getVelocity(), playerCharacter.getRadius(), "Test");
                            if(!readWriteObjectMode)
                            {
                                JSONObject jsonObject = SessionJSONUtil.getPlayerObjectJson(playerObject);
                                jsonObject.put("command", "updateposition");
                                sendUTF(jsonObject.toJSONString());
                            }
                            else
                            {
                                sendObject(playerObject);
                            }
                        }
                    }
                }
            }
        };
    }

    public boolean connect(String username)
    {
        try
        {
            if(!this.isConnected)
            {
                this.clientSocket = new Socket(this.host, this.port);
                this.isConnected = true;
                this.listenerThread.start();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("command", "setusername");
                jsonObject.put("username", username);
                sendUTF(jsonObject.toJSONString());

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
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void listen()
    {
        try
        {
            if(!this.readWriteObjectMode)
            {
                BufferedReader clientInput = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
                DataOutputStream clientOutput = new DataOutputStream(this.clientSocket.getOutputStream());

                String serverInput = clientInput.readLine();

                if(serverInput != null)
                {
                    receiveUTFData(serverInput);
                }
            }
            else
            {
                ObjectInputStream clientInput = new ObjectInputStream(this.clientSocket.getInputStream());
                ObjectOutputStream clientOutput = new ObjectOutputStream(this.clientSocket.getOutputStream());

                Object serverInput = clientInput.readObject();

                if(serverInput != null)
                {
                    receiveObjectData(serverInput);
                }
            }
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            disconnect();
        }
    }

    public void receiveUTFData(String data)
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

    public void receiveObjectData(Object data)
    {
        if(data != null)
        {
            if(this.receiver != null)
            {
                this.receiver.receive(data);
            }
            System.out.println("Client received: " + data);
        }
    }

    public void sendUTF(String data)
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

    public void sendObject(Object data)
    {
        try
        {
            if(this.isConnected)
            {
                ObjectOutputStream clientOutput = new ObjectOutputStream(this.clientSocket.getOutputStream());
                if(this.clientSocket != null)
                {
                    clientOutput.writeObject(data);
                    System.out.println("Client send: " + data);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("No connection found!");
        }
    }

    public void setWorld(World world)
    {
        this.world = world;
    }
}
