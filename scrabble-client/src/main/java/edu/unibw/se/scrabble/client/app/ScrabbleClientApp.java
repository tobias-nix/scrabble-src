package edu.unibw.se.scrabble.client.app;

import edu.unibw.se.scrabble.client.ccom.ClientCommunication;
import edu.unibw.se.scrabble.client.ccom.impl.ClientCommunicationImpl;
import edu.unibw.se.scrabble.client.view.View;
import edu.unibw.se.scrabble.client.view.impl.ViewControl;
import edu.unibw.se.scrabble.common.scom.NetworkConnect;

import java.rmi.Naming;

public class ScrabbleClientApp {

    private static final int PORT = 1099;

    public static void main(String[] args) {
        View view = new ViewControl();
        ClientCommunication clientCommunication = new ClientCommunicationImpl();

        view.setClientConnect(clientCommunication.getClientConnect());

        NetworkConnect networkConnect = null;
        try {
            networkConnect = (NetworkConnect) Naming.lookup("//127.0.0.1:1099/scrabble-server");
        } catch (Exception e) {
            e.printStackTrace();
        }

        clientCommunication.setNetworkConnect(networkConnect);

        view.show();
    }
}
