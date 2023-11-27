package edu.unibw.se.scrabble.server.auth;

import edu.unibw.se.scrabble.common.base.ReturnValues;
import edu.unibw.se.scrabble.server.data.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
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
        authDataTest = new AuthDataTest(null);
        //Anschließen der AuthDataTest bzw. AuthData an die Authentication
        authentication.setAuthData(authDataTest);

        //Anschließen der Credentials mit getCredentials()
        credentials = authentication.getCredentials();
    }

    @Test   //Prüft, ob die Initialisierung von Authentication und Credentials erfolgreich war
    void initValid() {
        assertNotNull(authentication);
        assertNotNull(credentials);
    }

    @Test
    void registerUserSuccessful() {
        String usernameTestdata = "julia";
        String passwordTestdata = "password1234!";
        assertEquals(ReturnValues.ReturnRegisterUser.SUCCESSFUL,
                credentials.registerUser(usernameTestdata, passwordTestdata));
        assertTrue(authDataTest.createUserIsCalled);
        assertEquals(authDataTest.createUserUsername, usernameTestdata);
        assertEquals(authDataTest.createUserPassword, passwordTestdata);
        assertTrue(authDataTest.usernameExists(usernameTestdata));
        assertTrue(authDataTest.usernameExistsIsCalled);
        assertEquals(authDataTest.usernameExistsUsername, usernameTestdata);
        assertEquals(authDataTest.getPassword(usernameTestdata), passwordTestdata);
        assertTrue(authDataTest.getPasswordIsCalled);
        assertEquals(authDataTest.getPasswordUsername, usernameTestdata);
    }

    @Test
    void registerUserInvalidInput() {
        assertEquals(ReturnValues.ReturnRegisterUser.FAILURE,
                credentials.registerUser(null, null));
        assertFalse(authDataTest.createUserIsCalled);
        assertFalse(authDataTest.getPasswordIsCalled);
        assertFalse(authDataTest.usernameExistsIsCalled);
    }

    @Test
    void registerUserInvalidUsernameShort() {
        assertEquals(ReturnValues.ReturnRegisterUser.INVALID_USERNAME,
                credentials.registerUser("lea", "password1234!"));
        assertFalse(authDataTest.createUserIsCalled);
        assertFalse(authDataTest.getPasswordIsCalled);
        assertFalse(authDataTest.usernameExistsIsCalled);
    }

    @Test
    void registerUserInvalidUsernameLong() {
        assertEquals(ReturnValues.ReturnRegisterUser.INVALID_USERNAME,
                credentials.registerUser("AnnaCharlotteBoessendoerfer", "password1234!"));
        assertFalse(authDataTest.createUserIsCalled);
        assertFalse(authDataTest.getPasswordIsCalled);
        assertFalse(authDataTest.usernameExistsIsCalled);
    }

    @Test
    void registerUserInvalidUsername() {
        assertEquals(ReturnValues.ReturnRegisterUser.INVALID_USERNAME,
                credentials.registerUser("Bo$$", "password1234!"));
        assertFalse(authDataTest.createUserIsCalled);
        assertFalse(authDataTest.getPasswordIsCalled);
        assertFalse(authDataTest.usernameExistsIsCalled);
    }

    @Test
    void registerUserAlreadyExists() {
        assertEquals(ReturnValues.ReturnRegisterUser.USERNAME_ALREADY_EXISTS,
                credentials.registerUser("paul", "password1234!"));
        assertFalse(authDataTest.createUserIsCalled);
        assertFalse(authDataTest.getPasswordIsCalled);
        assertTrue(authDataTest.usernameExistsIsCalled);
        assertEquals(authDataTest.usernameExistsUsername, "paul");
    }

    @Test
    void loginUserSuccessful() {
        String usernameTestdata = "paul";
        String passwordTestdata = "paulpaul1!";
        assertEquals(ReturnValues.ReturnLoginUser.SUCCESSFUL,
                credentials.loginUser(usernameTestdata, passwordTestdata));
        assertTrue(authDataTest.usernameExistsIsCalled);
        assertEquals(authDataTest.usernameExistsUsername, usernameTestdata);
        assertTrue(authDataTest.getPasswordIsCalled);
        assertEquals(authDataTest.getPasswordUsername, usernameTestdata);
        assertFalse(authDataTest.createUserIsCalled);
    }

    @Test
    void loginUserInvalidInput() {
        assertEquals(ReturnValues.ReturnLoginUser.FAILURE,
                credentials.loginUser(null, null));
        assertFalse(authDataTest.usernameExistsIsCalled);
        assertFalse(authDataTest.getPasswordIsCalled);
        assertFalse(authDataTest.createUserIsCalled);
    }

    @Test
    void loginUserNotExists() {
        assertEquals(ReturnValues.ReturnLoginUser.USERNAME_NOT_IN_DATABASE,
                credentials.loginUser("paula", "password1234!"));
        assertTrue(authDataTest.usernameExistsIsCalled);
        assertEquals(authDataTest.usernameExistsUsername, "paula");
        assertFalse(authDataTest.createUserIsCalled);
        assertFalse(authDataTest.getPasswordIsCalled);
    }

    @Test
    void loginUserWrongPassword() {
        assertEquals(ReturnValues.ReturnLoginUser.WRONG_PASSWORD,
                credentials.loginUser("berta", "password1234!"));
        assertTrue(authDataTest.usernameExistsIsCalled);
        assertEquals(authDataTest.usernameExistsUsername, "berta");
        assertTrue(authDataTest.getPasswordIsCalled);
        assertEquals(authDataTest.getPasswordUsername, "berta");
        assertFalse(authDataTest.createUserIsCalled);
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
        String createUserPassword = null;

        @Override
        public boolean createUser(String username, String password) {
            createUserIsCalled = true;
            createUserUsername = username;
            createUserPassword = password;

            if (authData != null) {       //reale Komponente
                return authData.createUser(username, password);
            } else {                     //Dummy
                throw new UnsupportedOperationException();
            }
        }
    }
}