package edu.unibw.se.scrabble.client.view.impl;

public class WindowFactory {
    public FxView createWindow(WindowState state, FxView mainView) throws Exception {
        return switch (state) {
            case LOGIN -> new LoginWindow(mainView);
            case REGISTER -> new RegisterWindow(mainView);
            case MAIN -> new MainWindow(mainView);
            case GAME -> new GameWindow(mainView);
            case STATISTICS -> new StatisticsWindow(mainView);
            case EXIT -> mainView.stop();
            default -> throw new IllegalArgumentException("Unknown view state: " + state);
        };
    }
}

