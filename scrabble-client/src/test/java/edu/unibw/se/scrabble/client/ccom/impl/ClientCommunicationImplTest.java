package edu.unibw.se.scrabble.client.ccom.impl;

import edu.unibw.se.scrabble.client.ccom.ClientCommunication;
import edu.unibw.se.scrabble.client.ccom.ClientCommunicationTest;
import edu.unibw.se.scrabble.common.scom.NetworkConnect;

public class ClientCommunicationImplTest extends ClientCommunicationTest {

    private static final ExampleClientCommunikation rmiClientCommunication = new ExampleClientCommunikation();
    @Override
    protected ClientCommunication getClientCommunication() {
        return rmiClientCommunication;
    }

    @Override
    protected NetworkConnect getNetworkConnect() {
        return null;
    }
}
