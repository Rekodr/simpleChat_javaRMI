package commands;

import client.RegistrationInfo;
import compute.IPresenceService;
import java.rmi.RemoteException;

public class Broadcast implements ICommand {
    public void execute(Object[] argv) throws RemoteException {
        System.out.println( "Hello world" );
        for(RegistrationInfo registration : ((IPresenceService)argv[0]).listRegisteredUsers()) {
            String local_name = ((RegistrationInfo)argv[1]).getUserName();
            String fried_name = registration.getUserName();
            if(!local_name.equals( fried_name )) {
                String status = registration.getStatus() ? "Available" : "Not available";
                System.out.println(registration.getUserName() + ": " + status);
            }
        }
    }
}
