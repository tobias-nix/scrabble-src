package edu.unibw.se.scrabble.client.ccom.impl;

import edu.unibw.se.scrabble.client.ccom.ClientCommunication;
import edu.unibw.se.scrabble.client.ccom.ClientCommunicationRealAuthRealDataTest;

public class ClientCommunicationImplRealAuthRealDataTest extends ClientCommunicationRealAuthRealDataTest {

    private static final ClientCommunicationImpl clientCommunication = new ClientCommunicationImpl();

    @Override
    protected ClientCommunication getClientCommunication() {
        return new ClientCommunicationImpl();
    }
}
