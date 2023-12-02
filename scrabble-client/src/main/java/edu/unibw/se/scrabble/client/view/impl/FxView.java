package edu.unibw.se.scrabble.client.view.impl;

import edu.unibw.se.scrabble.client.view.View;
import javafx.application.Application;
import javafx.stage.Stage;

public class FxView extends Application {
    private final WindowFactory windowFactory = new WindowFactory();
    private Stage currentView;
    private WindowState windowState = WindowState.LOGIN;

    @Override
    public void start(Stage stage) throws Exception {
        currentView = windowFactory.createWindow(windowState, this);
        currentView.showAndWait();
        //setWindowState(WindowState.LOGIN);
        //stage.setTitle("Scrabble");

        stage.show();
    }

    public void setWindowState(WindowState windowState){
        this.windowState = windowState;
    }

    public void switchToWindowState(WindowState state) throws Exception {
        setWindowState(state);
    }
}