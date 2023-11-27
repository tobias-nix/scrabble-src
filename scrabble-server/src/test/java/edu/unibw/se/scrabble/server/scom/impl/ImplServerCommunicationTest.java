package edu.unibw.se.scrabble.server.scom.impl;

import edu.unibw.se.scrabble.server.scom.ServerCommunication;
import edu.unibw.se.scrabble.server.scom.ServerCommunicationTest;

/**
 * @author Seegerer
 */
public class ImplServerCommunicationTest extends ServerCommunicationTest {
    private static final ServerCommunication serverCommunication = new ServerCommunicationImpl();
    @Override
    public ServerCommunication getServerCommunication() {
        return serverCommunication;
    }
}
