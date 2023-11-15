package edu.unibw.se.scrabble.client.view;

import edu.unibw.se.scrabble.client.ccom.ClientCommunication;
import edu.unibw.se.scrabble.client.ccom.ClientConnect;

/**
 * Interface for component View which is used to show the overlay of the game.
 *
 * @author Nix
 */
public interface View {

    /**
     * Add ClientCommunication interface to View so View can make requests to ClientCommunication to update the overlay.
     *
     * @param clientConnect the interface {@link ClientConnect}
     */
    void setClientConnect(ClientConnect clientConnect);

    /**
     * Triggers the update of the game overlay.
     */
    void show();
}
