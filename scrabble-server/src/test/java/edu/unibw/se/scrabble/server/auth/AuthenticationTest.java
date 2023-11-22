package edu.unibw.se.scrabble.server.auth;

import edu.unibw.se.scrabble.common.base.ReturnValues;
import edu.unibw.se.scrabble.server.data.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AuthenticationTest {

    private Authentication authentication;

    //Abstrakte Methode, die für die Erzeugung der konkreten Authentication verantwortlich ist (Factory Method).
    protected abstract Authentication getAuthentication();

    private Credentials credentials;

    private AuthDataTest authDataTest = null;

    //Abstrakte Methode, die für die Erzeugung der konkreten AuthData verantwortlich ist (Factory Method).
    protected abstract AuthData getAuthData();

    @BeforeEach
    void init() {
        authentication = getAuthentication();
        /*
        Erzeugt ein Objekt der Klasse AuthDataTest (siehe unten)
        Übergabeparameter ist getAuthData() kann null, Dummy oder reale Komponente sein.
         */
        authDataTest = new AuthDataTest(getAuthData());
        //Anschließen der AuthDataTest bzw. AuthData an die Authentication
        authentication.setAuthData(authDataTest);

        if (getAuthData() != null) {
            getAuthData().createUser("paul", "1234");
        }

        //Anschließen der Credentials mit getCredentials()
        credentials = authentication.getCredentials();
    }

    //Prüft, ob die Initialisierung von Authentication und Credentials erfolgreich war
    @Test
    void initValid() {
        assertNotNull(authentication);
        assertNotNull(credentials);
    }

    //TODO: Tests schreiben
    /*
    @Test
    void registerUserWithNullName() {
        assertEquals(ReturnValues.ReturnRegisterUser.USERNAME_ALREADY_EXISTS,       //TODO: Falscher Rückgabetyp
                credentials.registerUser(null, "1234"));
        assertFalse(authDataTest.createUserIsCalled);
    }
    */
    @Test
    void registerUserSuccessfull(){
        assertEquals(ReturnValues.ReturnRegisterUser.SUCCESSFUL,
                credentials.registerUser("paul", "1234"));      //TODO: Testdaten
        assertTrue(authDataTest.createUserIsCalled);
        assertEquals("paul", authDataTest.createUserUsername);
        assertEquals("1234", authDataTest.getCreateUserPassword);
    }

    @Test
    void loginUserSuccessfull(){
        assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL,
                credentials.loginUser("paul", "1234"));         //TODO: Testdaten
        assertTrue(authDataTest.usernameExistsIsCalled);
        assertEquals("paul", authDataTest.usernameExistsUsername);
        assertTrue(authDataTest.getPasswordIsCalled);
        assertEquals("1234", authDataTest.getPasswordUsername);
    }


    static class AuthDataTest implements AuthData {

        final AuthData authData;

        //Konstruktor ermöglicht Übergabe von realer Komponente (authData != null)
        AuthDataTest(AuthData authData) {
            this.authData = authData;
        }

        //Variablen tracken, ob die Methoden aufgerufen wurde und welche Parameter übergeben wurden
        boolean usernameExistsIsCalled = false;
        String usernameExistsUsername = null;

        @Override
        public boolean usernameExists(String username) {
            usernameExistsIsCalled = true;
            usernameExistsUsername = username;
            if (authData != null) {      //reale Komponente
                return authData.usernameExists(username);
            } else {                      //Dummy
                throw new UnsupportedOperationException();
            }
        }

        //Variablen tracken, ob die Methoden aufgerufen wurde und welche Parameter übergeben wurden
        boolean getPasswordIsCalled = false;
        String getPasswordUsername = null;

        @Override
        public String getPassword(String username) {
            getPasswordIsCalled = true;
            getPasswordUsername = username;

            if (authData != null) {       //reale Komponente
                return authData.getPassword(username);
            } else {
                throw new UnsupportedOperationException();
            }
        }

        //Variablen tracken, ob die Methoden aufgerufen wurde und welchen Parametern übergeben wurden
        boolean createUserIsCalled = false;
        String createUserUsername = null;
        String getCreateUserPassword = null;

        @Override
        public boolean createUser(String username, String password) {
            createUserIsCalled = true;
            createUserUsername = username;
            getCreateUserPassword = password;

            if (authData != null) {       //reale Komponente
                return authData.createUser(username, password);
            } else {                     //Dummy
                if (Objects.equals(username,"paul" ) && Objects.equals(password, "1234")) {  //TODO
                    return true;
                }
                throw new UnsupportedOperationException();
            }
        }
    }
}