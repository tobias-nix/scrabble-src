package edu.unibw.se.scrabble.client.view.impl;

import edu.unibw.se.scrabble.common.base.GameData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 @author Nix
 */

public class FxView extends Application {
    private final WindowFactory windowFactory = new WindowFactory();
    private Stage currentView;
    private WindowState windowState = WindowState.LOGIN;
    private String username;
    private int gameId;
    private char[] rackTiles;
    private char[] swapTiles;
    private GameData gameData;
    private String[] placedWords;

    @Override
    public void start(Stage stage) {
        setWindowState(windowState);
        ViewControl.cccI.setView(this);
    }
    public void setWindowState(WindowState windowState) {
        if (this.windowState == windowState && this.windowState == WindowState.WAIT)
            return;

        this.windowState = windowState;

        Platform.runLater(() -> {
            if (currentView != null) {
                currentView.close();
            }
            currentView = windowFactory.createWindow(windowState, this);
            currentView.setResizable(false);
            currentView.setMinWidth(1400);
            currentView.setMinHeight(800);
            currentView.setMaxWidth(1400);
            currentView.setMaxHeight(800);
            //currentView.setMaximized(true);

            currentView.setOnCloseRequest(event -> {
                currentView.close();
                Platform.exit();
                event.consume();
                System.exit(0);
            });
            currentView.show();
        });
    }

    public void setSendGameData(char[] rackTiles, char[] swapTiles, GameData gameData){
        this.rackTiles = rackTiles;
        this.swapTiles = swapTiles;
        this.gameData = gameData;
        setWindowState(WindowState.GAME);
    }

    public char[] getRackTiles(){
        return rackTiles;
    }

    public char[] getSwapTiles(){
        return swapTiles;
    }

    public GameData getGameData(){
        return gameData;
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
    public void setPlacedWords(String[] placedWords){
        this.placedWords = placedWords;
    }

    public String[] getPlacedWords(){
        return this.placedWords;
    }
}
