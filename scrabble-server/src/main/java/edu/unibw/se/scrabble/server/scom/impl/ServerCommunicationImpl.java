package edu.unibw.se.scrabble.server.scom.impl;

import edu.unibw.se.scrabble.common.scom.NetworkConnect;
import edu.unibw.se.scrabble.server.auth.Credentials;
import edu.unibw.se.scrabble.server.logic.ServerConnect;
import edu.unibw.se.scrabble.server.logic.ServerConnectCallback;
import edu.unibw.se.scrabble.server.scom.ServerCommunication;

public class ServerCommunicationImpl implements ServerCommunication {

    @Override
    public NetworkConnect getNetworkConnect() {
        return null;
    }

    @Override
    public void setServerConnect(ServerConnect serverConnect) {
        //serverConnect.setServerConnectCallback(this); //implements ServerConnectCallback
    }

    @Override
    public void setCredentials(Credentials credentials) {

    }
}
