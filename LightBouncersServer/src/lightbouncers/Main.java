package lightbouncers;

import lightbouncers.net.server.Server;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Server server = new Server(4509);
        server.start();
        boolean isRunning = true;

        Scanner scanner = new Scanner(System.in);

        while(isRunning)
        {
            if(scanner.hasNextLine())
            {
                String text = scanner.nextLine();

                switch(text.toLowerCase())
                {
                    case "stop":
                    {
                        server.stop();
                        isRunning = false;
                        break;
                    }
                }
            }
        }
    }
}
