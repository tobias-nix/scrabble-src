package edu.unibw.se.scrabble.server.scom.impl;

import edu.unibw.se.scrabble.server.scom.ServerCommunication;
import edu.unibw.se.scrabble.server.scom.ServerCommunicationTest;

import java.rmi.RemoteException;

/**
 * @author Seegerer
 */
public class ImplServerCommunicationTest extends ServerCommunicationTest {
    private static final ServerCommunication serverCommunication;

    static {
        try {
            serverCommunication = new ServerCommunicationImpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServerCommunication getServerCommunication() {
        return serverCommunication;
    }
}
