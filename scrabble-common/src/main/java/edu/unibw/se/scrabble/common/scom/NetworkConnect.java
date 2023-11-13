package edu.unibw.se.scrabble.common.scom;

import java.rmi.Remote;

public interface NetworkConnect extends Remote {
    ReturnLoginNetwork loginUser(String username, String password, ToClient toClient);
    enum ReturnLoginNetworkState {
        DATABASE_FAILURE,
        USERNAME_NOT_IN_DATABASE,
        WRONG_PASSWORD,
        SUCCESSFUL
    }
    // TODO null false in AuthData
    //  swaptile anlegen
    //  place tile umbenennen
    //  return place tile und swap tile umbenennen
    class ReturnLoginNetwork {
        public static ReturnLoginNetwork DATABASE_FAILURE =
                new ReturnLoginNetwork(ReturnLoginNetworkState.DATABASE_FAILURE);
        public static ReturnLoginNetwork USERNAME_NOT_IN_DATABASE =
                new ReturnLoginNetwork(ReturnLoginNetworkState.USERNAME_NOT_IN_DATABASE);
        public static ReturnLoginNetwork WRONG_PASSWORD =
                new ReturnLoginNetwork(ReturnLoginNetworkState.WRONG_PASSWORD);
        public final ReturnLoginNetworkState state;
        public final ToServer toServer;

        private ReturnLoginNetwork(ReturnLoginNetworkState state) {
            this.state = state;
            this.toServer = null;
        }

        public ReturnLoginNetwork(ReturnLoginNetworkState state, ToServer toServer) {
            this.state = state;
            if (this.state != ReturnLoginNetworkState.SUCCESSFUL) {
                this.toServer = null;
            } else {
                this.toServer = toServer;
            }

        }
    }

    ReturnRegisterNetwork registerUser(String username, String password, ToClient toClient);
    enum ReturnRegisterNetworkState {
        DATABASE_FAILURE,
        USERNAME_ALREADY_EXISTS,
        SUCCESSFUL
    }
    class ReturnRegisterNetwork {
        public static ReturnRegisterNetwork DATABASE_FAILURE =
                new ReturnRegisterNetwork(ReturnRegisterNetworkState.DATABASE_FAILURE);
        public static ReturnRegisterNetwork USERNAME_ALREADY_EXISTS =
                new ReturnRegisterNetwork(ReturnRegisterNetworkState.USERNAME_ALREADY_EXISTS);
        public final ReturnRegisterNetworkState state;
        public final ToServer toServer;

        private ReturnRegisterNetwork(ReturnRegisterNetworkState state) {
            this.state = state;
            this.toServer = null;
        }

        public ReturnRegisterNetwork(ReturnRegisterNetworkState state, ToServer toServer) {
            this.state = state;
            if (this.state != ReturnRegisterNetworkState.SUCCESSFUL) {
                this.toServer = null;
            } else {
                this.toServer = toServer;
            }

        }
    }
}
