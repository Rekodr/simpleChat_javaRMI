package client;

import commands.*;
import communication.MessageListener;
import compute.IPresenceService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Client {

    public static void main(String args[]) {

        HashMap<String, Object> commands = new HashMap<>();
        commands.put("friends", new Friends());
        commands.put("busy", new Busy());
        commands.put("available", new Available());
        commands.put("exit", new Exit());
        commands.put("talk", new Talk());
        commands.put("broadcast", new Broadcast());

        String user_name = args[1];
        if(user_name == null) {
            System.out.println( "Please provide a user name" );
            System.exit( -1 );
        }

        int port = 1999;
        try {
            port = Integer.parseInt( args[2] );
        } catch (NumberFormatException e) {
            System.out.println( "Default port 1999 used." );
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println( "Default port 1999 used." );
        }


        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {

            String name = "PresenceService";
            Registry registry = LocateRegistry.getRegistry(args[0]);
            IPresenceService presenceService = (IPresenceService) registry.lookup(name);


            RegistrationInfo reg = new RegistrationInfo(user_name, "localhost", port, true);
            boolean registered = presenceService.register(reg);

            if(!registered) {
                System.out.println("Faild to register. Please try letter");
                System.exit(-1);
            }

            MessageListener listener;
            try {
                listener = new MessageListener(reg);
                Thread thread = new Thread( listener );
                thread.start();

            } catch (IOException e) {
               presenceService.unregister( reg.getUserName() );
               System.out.println( "Port: " + port + " is taken. Please try a different port." );
               System.exit( -1 );
            }

            System.out.println(user_name + " joined the network.");
            //Enter data using BufferReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String input = reader.readLine();
                if(input != null) {
                    Object[] arg = input.split(" ");
                    if(commands.containsKey( arg[0] )) {
                        Object[] arguments = buidCmd( presenceService, reg, arg );
                        ICommand cmd = (ICommand) commands.get( arg[0] );
                        cmd.execute( arguments );
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("PresenceService exception:");
            e.printStackTrace();
        }

    }

    public static Object[] buidCmd(IPresenceService presenceService, RegistrationInfo reg, Object[] args) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(presenceService);
        params.add(reg);
        for(int i = 1; i < args.length; i++) {
            params.add(args[i]);
        }
       return params.toArray();
    }
}
