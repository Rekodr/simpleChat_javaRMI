package engine;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Vector;

import client.RegistrationInfo;
import compute.IPresenceService;
import compute.Task;

public class PresenceService implements IPresenceService {

    private HashMap<String, RegistrationInfo> registrations = new HashMap<>();

    public PresenceService() {
        super();
    }

    public boolean register(RegistrationInfo reg) throws RemoteException {
        if(reg == null) {
            return false;
        }
        return this.registrations.putIfAbsent(reg.getUserName(), reg) == null ? true : false;
    }

    public boolean updateRegistrationInfo(RegistrationInfo reg) throws RemoteException {
        if (reg == null) {
            return false;
        }

        if(this.registrations.containsKey(reg.getUserName())) {
            this.registrations.replace(reg.getUserName(), reg);
            return true;
        }
        return false;
    }

    public void unregister(String userName) throws RemoteException {
        if(userName != null) {
            this.registrations.remove(userName);
        }
    }

    public RegistrationInfo lookup(String name) throws RemoteException {
        if(name == null) {
            return null;
        }

        return this.registrations.get(name);
    }

    public Vector<RegistrationInfo> listRegisteredUsers() throws RemoteException {
        Vector<RegistrationInfo> v;
        v = new Vector<RegistrationInfo>( this.registrations.values() );
        return v;
    }


    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "PresenceService";
            IPresenceService engine = new PresenceService();
            IPresenceService stub =
                    (IPresenceService) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("PresenceService bound");
        } catch (Exception e) {
            System.err.println("PresenceService exception:");
            e.printStackTrace();
        }
    }
}

