package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.scom.NetworkConnect;

public interface ClientCommunication {

    /** Returns an interface for connecting to a client.
     * Never returns {@code null}.
     *
     * @return the interface ClientConnect ({@link ClientConnect})
     */
    ClientConnect getClientConnect();

    /**
     * Add NetworkConnect interface to ClientCommunication so Client can connect to network.
     *
     * @param networkConnect NetworkConnect interface
     */
    void setNetworkConnect(NetworkConnect networkConnect);
}
