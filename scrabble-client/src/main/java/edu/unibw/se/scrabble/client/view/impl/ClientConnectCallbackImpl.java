package edu.unibw.se.scrabble.client.view.impl;

import edu.unibw.se.scrabble.client.ccom.ClientConnectCallback;
import edu.unibw.se.scrabble.client.view.View;
import edu.unibw.se.scrabble.client.view.impl.FxView;
import edu.unibw.se.scrabble.client.view.impl.WindowState;
import edu.unibw.se.scrabble.common.base.GameData;

public class ClientConnectCallbackImpl implements ClientConnectCallback {

    private FxView view;

    public void setView(FxView view) {
        this.view = view;
    }

    @Override
    public void usersInSession(String[] usernames) {
        ViewControl.usernames = usernames;
        view.setWindowState(WindowState.WAIT); //TODO: Callback
    }

    @Override
    public void sendGameData(char[] rackTiles, char[] swapTiles, GameData gameData) {

    }

    @Override
    public void vote(String[] placedWords) {

    }
}
