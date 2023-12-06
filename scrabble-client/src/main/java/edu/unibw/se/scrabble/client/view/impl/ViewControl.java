package edu.unibw.se.scrabble.client.view.impl;

import edu.unibw.se.scrabble.client.ccom.ClientConnect;
import edu.unibw.se.scrabble.client.view.View;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableObjectValue;

import java.util.Arrays;
import java.util.Objects;

public class ViewControl implements View {
    static ClientConnect clientConnect = null;
    static ClientConnectCallbackImpl cccI = null;
    static String[] username = null;
    static StringProperty[] usernameLabel = null;

    @Override
    public void setClientConnect(ClientConnect clientConnect) {
        ViewControl.clientConnect = clientConnect;
        cccI = new ClientConnectCallbackImpl();

       // usernameLabel = new SimpleStringProperty[]{getUsername()};

        ViewControl.clientConnect.setClientConnectCallback(cccI);
    }

    @Override
    public void show() {
        Application.launch(FxView.class);
    }

    // Getter für die StringProperty
    public StringProperty[] usernameProperty() {
        return usernameLabel;
    }

    // Getter für den Benutzernamen
    public static String[] getUsername() {
        return Arrays.stream(usernameLabel).map(ObservableObjectValue::get).toArray(String[]::new);
    }

    // Setter für den Benutzernamen
    public static void setUsername(String[] username) {
        usernameLabel = Arrays.stream(username).map(SimpleStringProperty::new).toArray(SimpleStringProperty[]::new);
    }
}
