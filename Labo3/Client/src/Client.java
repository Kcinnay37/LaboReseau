import java.net.*;
import java.io.*;

public class Client
{
    //permet de rececoir et d'envoyer des info
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public Client(String address, int port)
    {
        try
        {
            //ouvre un socket qui ce connectera au serveur socket
            socket = new Socket(address, port);
            System.out.println("Connecter");

            in = new DataInputStream(System.in);
            out = new DataOutputStream(socket.getOutputStream());

            String line = "";

            while(!line.equals("Fini"))
            {
                try
                {
                    line = in.readLine();
                    out.writeUTF(line);
                }
                catch (IOException i)
                {
                    System.out.println(i);
                }
            }
            try
            {
                in.close();
                out.close();
                socket.close();
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
