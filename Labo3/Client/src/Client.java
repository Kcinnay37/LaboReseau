import java.net.*;
import java.io.*;

public class Client
{
    //permet de rececoir et d'envoyer des info
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    private String m_Protocol = "Yan";

    public Client(String address, int port)
    {
        try
        {
            //ouvre un socket qui ce connectera au serveur socket
            socket = new Socket(address, port);
            System.out.println("Connecter");

            in = new DataInputStream(System.in);
            DataInputStream inSocket = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            String line = "";

            while(!line.equals("Yan close"))
            {
                try
                {
                    line = in.readLine();
                    //ajoute le protole a la commande entré
                    line = m_Protocol + " " + line;
                    out.writeUTF(line);

                    CmdSend(line, inSocket);

                }
                catch (IOException i)
                {
                    System.out.println(i);
                    break;
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

    void CmdSend(String cmd, DataInputStream in)
    {
        String commande = "";
        for(int i = 0; i < cmd.length(); i++)
        {
            if(cmd.charAt(i) == ':')
            {
                break;
            }
            commande += cmd.charAt(i);
        }

        switch (commande)
        {
            case "Yan getAllFolder":
                PrintUTF(in);
                break;
            case "Yan read":
                PrintUTF(in);
                break;
            default:
                //default case
        }
    }

    void PrintUTF(DataInputStream in)
    {
        try
        {
            String test = in.readUTF();
            System.out.println(test);
        }
        catch (IOException i)
        {
            System.out.println(i);
        }
    }
}
