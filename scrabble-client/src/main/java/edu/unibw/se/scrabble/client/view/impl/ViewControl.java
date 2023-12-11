package edu.unibw.se.scrabble.client.view.impl;

import edu.unibw.se.scrabble.client.ccom.ClientConnect;
import edu.unibw.se.scrabble.client.view.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 @author Nix
 */

public class ViewControl implements View {
    static ClientConnect clientConnect = null;
    static ClientConnectCallbackImpl cccI = null;
    static final StringProperty[] usernameLabel = {
            new SimpleStringProperty("A"),
            new SimpleStringProperty("B"),
            new SimpleStringProperty("C"),
            new SimpleStringProperty("D"),
    };

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
    static void setUsername(String[] username) {
        Platform.runLater(() -> {
            if (username != null) {
                for (int i = 0; i < usernameLabel.length; i++) {
                    if (i < username.length && username[i] != null) {
                        usernameLabel[i].set(username[i]);
                    } else {
                        usernameLabel[i].set("");
                    }
                }
            }
        });
    }
}
