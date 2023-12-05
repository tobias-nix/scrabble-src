package edu.unibw.se.scrabble.client.view.impl;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class FxView extends Application {
    private final WindowFactory windowFactory = new WindowFactory();
    private Stage currentView;
    private WindowState windowState = WindowState.LOGIN;
    private String username;
    private int gameId;

    @Override
    public void start(Stage stage) {
        setWindowState(windowState);
    }

    public void setWindowState(WindowState windowState) {
        this.windowState = windowState;

        currentView = windowFactory.createWindow(windowState, this);
        currentView.setMaximized(true);

        currentView.setOnCloseRequest(event -> {
            currentView.close();
            Platform.exit();
            event.consume();
            System.exit(0);
        });

        currentView.show();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
