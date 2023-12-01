package edu.unibw.se.scrabble.server.data.impl;

import edu.unibw.se.scrabble.server.data.Data;
import edu.unibw.se.scrabble.server.data.DataTest;
import edu.unibw.se.scrabble.server.data.impl.spring.SpringScrabbleData;

/**
 * @author Bößendörfer
 */
public class SpringDataTest extends DataTest {

    private final SpringScrabbleData springScrabbleDataTest =
            new SpringScrabbleData("application-context-test.xml");

    @Override
    protected Data getData() {
        springScrabbleDataTest.clear();
        springScrabbleDataTest.fill();
        return springScrabbleDataTest;
    }
}
