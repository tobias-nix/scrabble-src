package edu.unibw.se.scrabble.client.ccom.impl;

import edu.unibw.se.scrabble.client.ccom.ClientCommunication;
import edu.unibw.se.scrabble.client.ccom.ClientCommunicationTest;
import edu.unibw.se.scrabble.common.scom.NetworkConnect;

import static org.junit.jupiter.api.Assertions.*;

class ExampleClientCommunikationTest extends ClientCommunicationTest {

    @Override
    public ClientCommunication getClientCommunication() {
        return new ExampleClientCommunikation();
    }

    @Override
    public NetworkConnect getNetworkConnect() {
        return null;
    }
}