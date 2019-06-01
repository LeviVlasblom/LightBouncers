package lightbouncers.net.newserver;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread
{
    private Socket clientSocket;

    //Data stream
    private BufferedReader dataInputStream;
    private PrintWriter dataOutputStream;

    //Object stream
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public ServerThread(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    public void run()
    {
        try
        {
            this.dataInputStream = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.dataOutputStream = new PrintWriter(this.clientSocket.getOutputStream());
            String clientInput = this.dataInputStream.readLine();

            if(!clientInput.isEmpty())
            {
                receive(clientInput);
            }
        }
        catch (IOException e)
        {
            System.out.println("IO Error/ Client terminated");
        }
        catch(NullPointerException e)
        {
            System.out.println("Client Closed");
        }
        finally
        {
            closeConnection();
        }
    }

    public void send(String data)
    {
        if(this.clientSocket != null)
        {
            this.dataOutputStream.println(data);
            this.dataOutputStream.flush();
            System.out.println("Server send: " + data);
        }
    }

    private void receive(String data)
    {

    }

    private void closeConnection()
    {
        try
        {
            System.out.println("Connection Closing..");
            if (this.dataInputStream!=null)
            {
                this.dataInputStream.close();
                System.out.println("Socket Input Stream Closed");
            }

            if(this.dataOutputStream!=null)
            {
                this.dataOutputStream.close();
                System.out.println("Socket Out Closed");
            }
            if (this.clientSocket!=null)
            {
                clientSocket.close();
                System.out.println("Socket Closed");
            }

        }
        catch(IOException ie)
        {
            System.out.println("Socket Close Error");
        }
    }
}
