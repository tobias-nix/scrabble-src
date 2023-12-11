package edu.unibw.se.scrabble.server.auth.impl;

import edu.unibw.se.scrabble.server.auth.Authentication;
import edu.unibw.se.scrabble.server.auth.AuthenticationTest;
import edu.unibw.se.scrabble.server.data.AuthData;
import edu.unibw.se.scrabble.server.data.Data;
import edu.unibw.se.scrabble.server.data.impl.spring.SpringScrabbleData;

public class AuthenticationImplTest extends AuthenticationTest {

    private static final Authentication authentication = new AuthenticationImpl();
    //private static final Data data = new DataHashMap();
    private static final SpringScrabbleData data = new SpringScrabbleData();


    @Override
    protected Authentication getAuthentication() {
        return authentication;
    }

    @Override
    protected AuthData getAuthData() {
        data.clear();
        data.fill();
        return ((Data)data).getAuthData();
    }
}
