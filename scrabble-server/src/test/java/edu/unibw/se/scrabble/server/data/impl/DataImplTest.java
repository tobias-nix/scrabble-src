package edu.unibw.se.scrabble.server.data.impl;

import edu.unibw.se.scrabble.server.data.Data;
import edu.unibw.se.scrabble.server.data.DataTest;
import edu.unibw.se.scrabble.server.data.impl.hashMap.DataHashMap;

/**
 @author Bößendörfer
 */
public class DataImplTest extends DataTest {

    private static final Data data = new DataHashMap();

    @Override
    protected Data getData() {
        return data;
    }
}
