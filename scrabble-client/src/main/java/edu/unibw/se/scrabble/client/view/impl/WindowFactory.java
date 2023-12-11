package edu.unibw.se.scrabble.client.view.impl;

import javafx.stage.Stage;

/**
 @author Nix
 */

public class WindowFactory {
    public Stage createWindow(WindowState state, FxView mainView) {
        return switch (state) {
            case LOGIN -> new LoginWindow(mainView);
            case REGISTER -> new RegisterWindow(mainView);
            case MAIN -> new MainWindow(mainView);
            case WAIT -> new WaitWindow(mainView);
            case GAME -> new GameWindow(mainView);
            //case STATISTICS -> new StatisticsWindow(mainView);
            default -> throw new IllegalArgumentException("Unknown view state: " + state);
        };
    }
}

