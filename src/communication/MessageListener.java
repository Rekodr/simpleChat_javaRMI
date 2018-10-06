package communication;

import client.RegistrationInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageListener implements Runnable {

    private boolean status ;
    private int port = 1999;
    private ServerSocket echoServer = null;
    public MessageListener(RegistrationInfo reg) throws IOException {
        this.status = reg.getStatus();
        this.port = reg.getPort();

        echoServer = new ServerSocket(this.port);
    }

    @Override
    public void run() {
        Socket clientSocket = null;

        while(true) {
            if(status) try {
                System.out.println( "Waiting for connections" );
                clientSocket = echoServer.accept();
                Thread thread = new Thread( new ProcessIncomingRequest( clientSocket ) );
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

