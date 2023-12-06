package edu.unibw.se.scrabble.client.view.impl;

import edu.unibw.se.scrabble.client.ccom.ClientConnect;
import edu.unibw.se.scrabble.client.view.View;
import javafx.application.Application;

public class ViewControl implements View {
    static ClientConnect clientConnect = null;
    static ClientConnectCallbackImpl cccI = null;
    static String[] usernames = null;

    @Override
    public void setClientConnect(ClientConnect clientConnect) {
        ViewControl.clientConnect = clientConnect;
        cccI = new ClientConnectCallbackImpl();

        ViewControl.clientConnect.setClientConnectCallback(cccI);
    }

    @Override
    public void show() {
        Application.launch(FxView.class);
    }
}
