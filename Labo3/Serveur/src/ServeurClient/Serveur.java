package ServeurClient;
import java.net.*;
import java.io.*;

public class Serveur
{
    //recoi et envoi les info
    private Socket socket = null;

    //le socket qui est ouvert qui permet douvrir la connection et permettre decouter si quelqun
    //veux ce connecter a la connection c'est aussi lui qui va accepter si quelqun demande de ce connecter
    private ServerSocket server = null;
    private DataInputStream in = null;

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

            String line = "";

            while(!line.equals("Fini"))
            {
                try
                {
                    //retourn le output du client
                    line = in.readUTF();
                    System.out.println(line);
                }
                catch (IOException i)
                {
                    System.out.println(i);
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
}
