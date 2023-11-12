package edu.unibw.se.scrabble.client.view;

import edu.unibw.se.scrabble.client.ccom.ClientCommunication;

/**
 * Interface for component View which is used to show the overlay of the game.
 */
public interface View {

    /**
     * Add ClientCommunication interface to View so View can make requests to ClientCommunication to update the overlay.
     * @param communication
     */
    void setClientConnect(ClientCommunication communication);

    /**
     * Triggers the update of the game overlay.
     */
    void show();
}
