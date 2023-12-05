package edu.unibw.se.scrabble.client.ccom.impl;

import edu.unibw.se.scrabble.client.ccom.ClientCommunication;
import edu.unibw.se.scrabble.client.ccom.ClientCommunicationFillTest;

public class ClientCommunicationFillImplTest extends ClientCommunicationFillTest {

    @Override
    protected ClientCommunication getClientCommunication() {
        return new ClientCommunicationImpl();
    }
}
