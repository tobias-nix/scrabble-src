package edu.unibw.se.scrabble.client.view;

import edu.unibw.se.scrabble.client.ccom.ClientConnect;
import edu.unibw.se.scrabble.client.ccom.ClientConnectCallback;
import edu.unibw.se.scrabble.client.view.impl.*;
import edu.unibw.se.scrabble.common.base.*;

class FxViewTest {
    public static void main(String[] args) {
        View view = new ViewControl();
        view.setClientConnect(new ClientConnectImpl());

        view.show();
    }

    static class ClientConnectImpl implements ClientConnect {


        @Override
        public void setClientConnectCallback(ClientConnectCallback clientConnectCallback) {

        }

        @Override
        public ReturnValues.ReturnLoginUser loginUser(String username, String password) {
            if (username.startsWith("a")) {
                return ReturnValues.ReturnLoginUser.INVALID_PASSWORD;
            } else if (username.startsWith("b")) {
                return ReturnValues.ReturnLoginUser.INVALID_USERNAME;
            } else if (username.startsWith("c")) {
                return ReturnValues.ReturnLoginUser.FAILURE;
            } else if (username.startsWith("d")) {
                return ReturnValues.ReturnLoginUser.DATABASE_FAILURE;
            } else if (username.startsWith("e")) {
                return ReturnValues.ReturnLoginUser.NETWORK_FAILURE;
            } else if (username.startsWith("f")) {
                return ReturnValues.ReturnLoginUser.WRONG_PASSWORD;
            } else if (username.startsWith("g")) {
                return ReturnValues.ReturnLoginUser.USERNAME_NOT_IN_DATABASE;
            } else {
                return ReturnValues.ReturnLoginUser.SUCCESSFUL;
            }
        }

        @Override
        public ReturnValues.ReturnRegisterUser registerUser(String username, String password) {
            return null;
        }

        @Override
        public ReturnValues.ReturnStatistics getUserStatistics() {
            return null;
        }

        @Override
        public ReturnValues.ReturnCreateSession createSession(LanguageSetting languageSetting) {
            return null;
        }

        @Override
        public ReturnValues.ReturnJoinSession joinSession(int gameId) {
            return null;
        }

        @Override
        public ReturnValues.ReturnStartGame startGame() {
            return null;
        }

        @Override
        public ReturnValues.ReturnSelectAction selectAction(ActionState actionState) {
            return null;
        }

        @Override
        public ReturnValues.ReturnPlaceTile placeTile(TileWithPosition tileWithPosition) {
            return null;
        }

        @Override
        public ReturnValues.ReturnSwapTile swapTile(char letter) {
            return null;
        }

        @Override
        public ReturnValues.ReturnEndTurn endTurn() {
            return null;
        }

        @Override
        public ReturnValues.ReturnSendPlayerVote sendPlayerVote(PlayerVote playerVote) {
            return null;
        }
    }
}