package edu.unibw.se.scrabble.client.view;

import edu.unibw.se.scrabble.client.ccom.ClientCommunication;

public interface View {
    void setClientConnect(ClientCommunication communication);

    void show();
}
