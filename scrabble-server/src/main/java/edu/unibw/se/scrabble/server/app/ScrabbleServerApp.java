package edu.unibw.se.scrabble.server.app;

import edu.unibw.se.scrabble.server.auth.Authentication;
import edu.unibw.se.scrabble.server.auth.impl.AuthenticationImpl;
import edu.unibw.se.scrabble.server.data.impl.spring.SpringScrabbleData;
import edu.unibw.se.scrabble.server.logic.ServerConnect;
import edu.unibw.se.scrabble.server.logic.ServerLogic;
import edu.unibw.se.scrabble.server.logic.impl.ServerLogicImpl;
import edu.unibw.se.scrabble.server.scom.ServerCommunication;
import edu.unibw.se.scrabble.server.scom.impl.ServerCommunicationImpl;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ScrabbleServerApp {
    static final int PORT = 1099;
    public static void main(String[] args) {
        ServerCommunication serverCommunication;
        try {
            serverCommunication = new ServerCommunicationImpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        /*
        ServerLogic serverLogic = new ServerLogicImpl();
        ServerConnect serverConnect = serverLogic.getServerConnect();
        serverCommunication.setServerConnect(serverConnect);

        Authentication authentication = new AuthenticationImpl();
        SpringScrabbleData springDatabase = new SpringScrabbleData();
        springDatabase.clear();

        authentication.setAuthData(springDatabase.getAuthData());
        serverCommunication.setCredentials(authentication.getCredentials());
        */

        serverCommunication.setServerConnect((new ServerLogicImpl()).getServerConnect());

        Authentication authentication = new AuthenticationImpl();
        authentication.setAuthData((new SpringScrabbleData()).getAuthData());
        serverCommunication.setCredentials(authentication.getCredentials());


        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            System.out.println("Registry ready");
        } catch (Exception e) {
            System.out.println("Registry exception: " + e.getMessage());
        }

        try {
            Naming.rebind("//127.0.0.1:" + PORT + "/scrabble-server", serverCommunication.getNetworkConnect());
            System.out.println("[scrabble-server] eingetragen.");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
