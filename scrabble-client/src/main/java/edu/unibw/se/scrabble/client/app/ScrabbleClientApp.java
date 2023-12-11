package edu.unibw.se.scrabble.client.app;

import edu.unibw.se.scrabble.client.ccom.ClientCommunication;
import edu.unibw.se.scrabble.client.ccom.impl.ClientCommunicationImpl;
import edu.unibw.se.scrabble.client.view.View;
import edu.unibw.se.scrabble.client.view.impl.ViewControl;
import edu.unibw.se.scrabble.common.scom.NetworkConnect;

import java.rmi.Naming;

/**
 @author Nix
 */

public class ScrabbleClientApp {

    private static final int PORT = 1099;

    public static void main(String[] args) {
        View view = new ViewControl();
        ClientCommunication clientCommunication = new ClientCommunicationImpl();



        NetworkConnect networkConnect = null;
        try {
            networkConnect = (NetworkConnect) Naming.lookup("//127.0.0.1:" + PORT + "/scrabble-server");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        view.setClientConnect(clientCommunication.getClientConnect());

        clientCommunication.setNetworkConnect(networkConnect);

        view.show();
    }
}
