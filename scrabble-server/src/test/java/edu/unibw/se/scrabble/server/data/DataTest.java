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

       /* @Test
        //order
        void userStatisticsTest() {
            String username = "paul";
            Statistics testStatistics = new Statistics(3,1,100,150);
            assertTrue(scrabbleData.saveUserStatistics(username, testStatistics));
            Statistics playersStatistics = scrabbleData.getUserStatistics(username);
            assertNotNull(playersStatistics);
            assertEquals(playersStatistics, testStatistics);
            assertEquals(testStatistics.gamesPlayed(), playersStatistics.gamesPlayed());
            assertEquals(testStatistics.gamesWon(), playersStatistics.gamesWon());
            assertEquals(testStatistics.highestScore(), playersStatistics.highestScore());
            assertEquals(testStatistics.totalScore(), playersStatistics.totalScore());
        }*/

        @Test
        void passwordTest(){
            String returnPassword = authData.getPassword("paul");
            assertEquals(returnPassword, "1234");
        }
    }

}