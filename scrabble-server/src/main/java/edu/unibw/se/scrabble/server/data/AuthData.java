package edu.unibw.se.scrabble.server.data;

public interface AuthData {
    /*
    Soll benutzt werden, um zu überprüfen, ob Username in der Datenbank vorhanden ist.
     */
    boolean usernameExists(String username);

    /*
    Gibt passwort zurück
     */
    String getPassword(String username);

    /*
    Neuen User anlegen, Statistiken mit 0 initialisieren nicht vergessen
     */
    boolean createUser(String username, String password);
}
