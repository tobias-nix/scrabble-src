package edu.unibw.se.scrabble.client.view.impl;

import edu.unibw.se.scrabble.client.ccom.ClientConnect;
import edu.unibw.se.scrabble.client.view.View;
import javafx.application.Application;

public class ViewControl implements View {
    static ClientConnect clientConnect = null;

    @Override
    public void setClientConnect(ClientConnect clientConnect) {
        clientConnect = clientConnect;
    }

    @Override
    public void show() {
        Application.launch(FxView.class);
    }
}
