package commands;

import java.rmi.RemoteException;

public class Talk implements ICommand {
    public void execute(Object[] argv) throws RemoteException {
        System.out.println("Talk");
    }
}
