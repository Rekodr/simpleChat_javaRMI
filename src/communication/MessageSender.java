package communication;

import client.RegistrationInfo;
import compute.IPresenceService;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

public class MessageSender implements Runnable{

    private String message;
    private String host;
    private int port;

    public MessageSender(String msg, RegistrationInfo dst_reg) {
        this.message = msg;
        this.port = dst_reg.getPort();
        this.host = dst_reg.getHost();
    }

    public void send() {
        try {
            PrintStream os;

            System.out.println("Sending message...");
            Socket clientSocket = new Socket(this.host, this.port);

            os = new PrintStream(clientSocket.getOutputStream());

            if(this.message  == null) {
                return;
            }
            os.println(this.message);
            System.out.println( "Message sent." );

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(RegistrationInfo src, String dst_username, IPresenceService presenceService, String msg) throws RemoteException {

        RegistrationInfo dst_reg = presenceService.lookup( dst_username );

        if(dst_reg == null) {
            System.out.println( "Destination user is not registered." );
            return;
        }

        if(!dst_reg.getStatus()) {
            System.out.println( "Destination user is not available." );
        }



        MessageSender sender = new MessageSender( msg, dst_reg );
        Thread thread = new Thread( sender );
        thread.start();

    }

    @Override
    public void run() {
        this.send();
    }
}

