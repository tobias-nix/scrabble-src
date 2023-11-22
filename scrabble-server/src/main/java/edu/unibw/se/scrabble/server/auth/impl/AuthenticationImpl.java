package edu.unibw.se.scrabble.server.auth.impl;

import edu.unibw.se.scrabble.common.base.ReturnValues;
import edu.unibw.se.scrabble.server.auth.Authentication;
import edu.unibw.se.scrabble.server.auth.Credentials;
import edu.unibw.se.scrabble.server.data.AuthData;

public class AuthenticationImpl implements Authentication, AuthData, Credentials {
    @Override
    public Credentials getCredentials() {
        return this;
    }

    @Override
    public void setAuthData(AuthData authData) {

    }

    @Override
    public ReturnValues.ReturnLoginUser loginUser(String username, String password) {
        return null;
    }

    @Override
    public ReturnValues.ReturnRegisterUser registerUser(String username, String password) {
        return ReturnValues.ReturnRegisterUser.SUCCESSFUL;
    }

    @Override
    public boolean usernameExists(String username) {
        return false;
    }

    @Override
    public String getPassword(String username) {
        return null;
    }

    @Override
    public boolean createUser(String username, String password) {
        return false;
    }
}
