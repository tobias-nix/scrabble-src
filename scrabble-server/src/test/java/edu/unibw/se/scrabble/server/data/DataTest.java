package edu.unibw.se.scrabble.server.data;

import edu.unibw.se.scrabble.common.base.Statistics;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Data Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class DataTest {

    private static Data data;
    private static AuthData authData;
    private static ScrabbleData scrabbleData;

    //Abstrakte Methode, die für die Erzeugung der konkreten Data verantwortlich ist (Factory Method).
    protected abstract Data getData();

    @BeforeAll
    void init() {
        /*
        Zur Erzeugung der konkreten Instanz wird auf die abstrakte Erzeugermethode zurückgegriffen.
        Klasse DataTestImpl implementiert mögliche Erzeugermethode getData()
         */
        data = getData();
        authData = data.getAuthData();
        scrabbleData = data.getScrabbleData();
    }

    @Nested
    class Tests {
        @Test
        void initTest() {
            assertNotNull(data);
            assertNotNull(authData);
            assertNotNull(scrabbleData);
        }

        @Test
        void getPasswordWithValidUsername() {
            String returnPassword = authData.getPassword("paul");
            assertEquals(returnPassword, "paulpaul1!");
        }

        @Test
        void getPasswordWithInvalidUsername() {
            String returnPassword = authData.getPassword("herbert");
            assertNull(returnPassword);
        }

        @Test
        void getPasswordWithNullUsername() {
            String returnPassword = authData.getPassword(null);
            assertNull(returnPassword);
        }

        @Test
        void usernameExistsWithNull() {
            boolean returnUsername = authData.usernameExists(null);
            assertFalse(returnUsername);
        }

        @Test
        void usernameExistsWithValidUsername() {
            boolean returnUsername = authData.usernameExists("paul");
            assertTrue(returnUsername);
        }

        @Test
        void usernameExistsWithInvalidUsername() {
            boolean returnUsername = authData.usernameExists("herbert");
            assertFalse(returnUsername);
        }

        @Test
        void createUserWithValidInput() {
            boolean returnCreateUser = authData.createUser("franz", "password123!");
            assertTrue(returnCreateUser);
        }

        @Test
        void createUserWithInvalidUsername() {
            boolean returnCreateUser = authData.createUser("lea", "password123!");
            assertFalse(returnCreateUser);
        }

        @Test
        void createUserWithNull() {
            boolean returnCreateUser = authData.createUser(null, null);
            assertFalse(returnCreateUser);
        }

        @Test
        void getUserStatisticsValid() {
            Statistics returnStatistics = scrabbleData.getUserStatistics("anna");
            Statistics testStatistics = new Statistics(44, 4, 444, 4444);
            assertEquals(returnStatistics, testStatistics);
        }

        @Test
        void saveUserStatisticsValid() {
            boolean returnSaveUserStatistics = scrabbleData.saveUserStatistics("berta",
                    new Statistics(99, 9, 999, 9999));
            assertTrue(returnSaveUserStatistics);
        }

        @Test
        void saveUserStatisticsInvalid() {
            boolean returnSaveUserStatistics = scrabbleData.saveUserStatistics("klaus",
                    new Statistics(99, 9, 999, 9999));
            assertFalse(returnSaveUserStatistics);
        }

    }

}