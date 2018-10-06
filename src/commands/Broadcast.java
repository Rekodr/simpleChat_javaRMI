package commands;

import client.RegistrationInfo;
import communication.MessageSender;
import compute.IPresenceService;
import java.rmi.RemoteException;

public class Broadcast implements ICommand {
    public void execute(Object[] argv) throws RemoteException {
        try {
            RegistrationInfo src = (RegistrationInfo)argv[1];
            String msg = "[ " + src.getUserName() + " ] ";
            String temp = "";
            // build the message.
            for(int i = 2; i < argv.length; i++) {
                temp += (String) argv[i] + " ";
            }

            if(temp.trim().isEmpty()) {
                System.out.println( "Cannot send empty msg." );
                return;
            }

            msg += temp ;

            for(RegistrationInfo registration : ((IPresenceService)argv[0]).listRegisteredUsers()) {
                String fried_name = registration.getUserName();
                if(!src.getUserName().equals( fried_name )) {
                    if(registration.getStatus()) {
                        MessageSender.sendMessage( src, registration.getUserName(), (IPresenceService)argv[0], msg );
                    }
                }
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println( "Please provide receiver and message." );
        }
    }
}
