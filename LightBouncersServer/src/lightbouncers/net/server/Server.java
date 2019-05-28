package lightbouncers.net.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server
{
    private ITCPServerReceiver receiver;
    private int port;
    private ServerSocket serverSocket;
    private Thread connectionThread;
    private ArrayList<Thread> listenerThreads;
    private ArrayList<Socket> clientSockets;

    private boolean isRunning;

    public Server(int port, ITCPServerReceiver receiver)
    {
        this.port = port;
        this.isRunning = false;
        this.listenerThreads = new ArrayList<Thread>();
        this.clientSockets = new ArrayList<Socket>();
        this.receiver = receiver;
    }

    public void start()
    {
        try
        {
            if(!this.isRunning)
            {
                this.serverSocket = new ServerSocket(this.port);
                this.isRunning = true;
                listenForConnections();
                System.out.println("Server started on port: " + this.port);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void stop()
    {
        try
        {
            if(this.isRunning)
            {
                this.isRunning = false;
                for(Thread listenerThread : this.listenerThreads)
                {
                    listenerThread = null;
                }
                if(this.connectionThread != null)
                {
                    this.connectionThread = null;
                }

                if(this.serverSocket != null)
                {
                    this.serverSocket.close();
                    System.out.println("Server stopped on port: " + this.port);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void broadcast(String data)
    {
        for(Socket socket : this.clientSockets)
        {
            try
            {
                DataOutputStream serverOutput = new DataOutputStream(socket.getOutputStream());
                serverOutput.writeBytes(data + '\n');
                System.out.println("Server broadcasts: " + data);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void send(String data, Socket socket)
    {
        try
        {
            if(socket != null)
            {
                DataOutputStream serverOutput = new DataOutputStream(socket.getOutputStream());
                serverOutput.writeBytes(data + '\n');
                System.out.println("Server send: " + data);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void receive(String data, Socket socket)
    {
        try
        {
            if(socket != null)
            {
                if(this.receiver != null)
                {
                    this.receiver.receive(data, socket);
                }
                System.out.println("Server received: " + data);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void listenForConnections()
    {
        this.connectionThread = new Thread("SocketConnectionListener")
        {
            public void run()
            {
                while(isRunning)
                {
                    try
                    {
                        Socket clientSocket = serverSocket.accept();

                        clientSockets.add(clientSocket);
                        System.out.println("Client connected with ip: " + clientSocket.getInetAddress().getHostAddress() + " on port: " + clientSocket.getPort());

                        Thread listenerThread = new Thread(){
                            public void run()
                            {
                                while(isRunning)
                                {
                                    listen(clientSocket);
                                    try {
                                        Thread.sleep(10);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        };

                        listenerThreads.add(listenerThread);
                        listenerThread.start();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        this.connectionThread.start();
    }

    synchronized private void listen(Socket socket)
    {
        try
        {
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream serverOutput = new DataOutputStream(socket.getOutputStream());

            String clientInput = serverInput.readLine();

            if(clientInput != null)
            {
                receive(clientInput, socket);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
