package ServeurClient;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Serveur
{
    //recoi et envoi les info
    private Socket socket = null;

    //le socket qui est ouvert qui permet douvrir la connection et permettre decouter si quelqun
    //veux ce connecter a la connection c'est aussi lui qui va accepter si quelqun demande de ce connecter
    private ServerSocket server = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    private String m_Protocol = "Yan";

    public Serveur(int port)
    {
        try
        {
            //ouvre un serveur socket qui ecouteras pour une conection extern
            server = new ServerSocket(port);

            System.out.println("En attente d'une connection");
            //block le code jusqua une connection extern sois effectuer
            //et retourne le socket qui permetera la connection client serveur
            socket = server.accept();
            System.out.println("Client connecter");

            //permet d'aller voir ce que le socket recois
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            String line = "";

            String endCondition = m_Protocol + " close";
            while(!line.equals(endCondition))
            {
                try
                {
                    //retourn le output du client
                    line = in.readUTF();
                    CmdReceive(line, out);
                }
                catch (IOException i)
                {
                    System.out.println(i);
                    break;
                }
            }
            System.out.println("Connection Fermer");

            socket.close();
            in.close();
        }
        catch (IOException a)
        {
            System.out.println(a);
        }
    }

    private void CmdReceive(String cmd, DataOutputStream out)
    {
        System.out.println(cmd);
        if(CheckProtocole(cmd))
        {
            String commande = "";
            int i;
            for(i = 0; i < cmd.length(); i++)
            {
                if(cmd.charAt(i) == ':')
                {
                    i++;
                    break;
                }
                commande += cmd.charAt(i);
            }

            String fileName = "";
            for(;i < cmd.length(); i++)
            {
                if(cmd.charAt(i) == ':')
                {
                    i++;
                    break;
                }
                fileName += cmd.charAt(i);
            }

            switch (commande)
            {
                //cree un fichier
                case "Yan createFile":
                    CreateFile(fileName);
                    break;
                case "Yan getAllFolder":
                        GetAllFolder(out);
                    break;
                case "Yan write":
                    String text = "";
                    for(;i < cmd.length(); i++)
                    {
                        text += cmd.charAt(i);
                    }
                    WriteFile(fileName, text);
                    break;
                case "Yan read":
                    ReadFile(fileName, out);
                    break;
                default:
                    //default case
            }
        }
    }

    private boolean CheckProtocole(String cmd)
    {
        if(cmd.length() >= m_Protocol.length())
        {
            String protocol = "";
            for(int i = 0; i < m_Protocol.length(); i++)
            {
                protocol += cmd.charAt(i);
            }
            if(protocol.equals(m_Protocol))
            {
                System.out.println("Protocol is good");
                return true;
            }
        }
        System.out.println("Protocol is not good");
        return false;
    }

    private void CreateFile(String fileName)
    {
        File newFile = new File("FileCreate\\" + fileName);
        try
        {
            if(newFile.createNewFile())
            {
                System.out.println("File created: " + newFile.getName());
            }
            else
            {
                System.out.println("File already exists.");
            }
        }
        catch (IOException a)
        {
            System.out.println(a);
        }
    }

    private void GetAllFolder(DataOutputStream out)
    {
        try
        {
            File file = new File("FileCreate");
            String[] tabl = file.list();

            String text = "";

            for(int i = 0; i < tabl.length; i++)
            {
                text += tabl[i];
                if(i != tabl.length - 1)
                {
                    text += '\n';
                }
            }

            out.writeUTF(text);
        }
        catch (IOException i)
        {
            System.out.println(i);
        }
    }

    private void WriteFile(String fileName, String text)
    {
        System.out.println("ca peut etre marcher");
        try
        {
            FileWriter writer = new FileWriter("FileCreate\\" + fileName);
            writer.write(text);
            writer.close();
            System.out.println("Successfully wrote to the file.");
        }
        catch (IOException a)
        {
            System.out.println(a);
        }
    }

    private void ReadFile(String fileName, DataOutputStream out)
    {
        try
        {
            File file = new File("FileCreate\\" + fileName);
            Scanner reader = new Scanner(file);

            String text = "";

            while (reader.hasNextLine())
            {
                text += reader.nextLine();
            }
            out.writeUTF(text);
        }
        catch (IOException i)
        {
            System.out.println(i);
        }
    }
}
