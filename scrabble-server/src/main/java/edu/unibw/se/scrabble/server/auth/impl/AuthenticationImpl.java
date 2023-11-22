package edu.unibw.se.scrabble.server.auth.impl;

import edu.unibw.se.scrabble.common.base.ReturnValues;
import edu.unibw.se.scrabble.server.auth.Authentication;
import edu.unibw.se.scrabble.server.auth.Credentials;
import edu.unibw.se.scrabble.server.data.AuthData;

import java.util.Objects;
import java.util.regex.Pattern;

public class AuthenticationImpl implements Authentication, AuthData, Credentials {

    private AuthData authData = null;

    @Override
    public Credentials getCredentials() {
        return this;
    }

    @Override
    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    @Override
    public ReturnValues.ReturnLoginUser loginUser(String username, String password) {
        if (username == null || password == null) {
            return ReturnValues.ReturnLoginUser.DATA_FORMAT_FAILURE;
        }
        if (!authData.usernameExists(username)) {
            return ReturnValues.ReturnLoginUser.USERNAME_NOT_IN_DATABASE;
        }
        if (!Objects.equals(authData.getPassword(username),password)) {
            return ReturnValues.ReturnLoginUser.WRONG_PASSWORD;
        }
        return ReturnValues.ReturnLoginUser.SUCCESSFUL;
    }

    private static final Pattern NAME_PATTERN = Pattern.compile("[A-Za-z0-9_]{4,15}");
    //Password-Format muss vermutlich schon vorher geprüft werden
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("[A-Za-z?!$%&/()*0-9]{8,20}");

    @Override
    public ReturnValues.ReturnRegisterUser registerUser(String username, String password) {
        if (username == null || !NAME_PATTERN.matcher(username).matches() ||
                password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            return ReturnValues.ReturnRegisterUser.DATA_FORMAT_FAILURE;
        }
        if (authData.usernameExists(username)) {
            return ReturnValues.ReturnRegisterUser.USERNAME_ALREADY_EXISTS;
        }
        if (authData.createUser(username, password)) {
            return ReturnValues.ReturnRegisterUser.SUCCESSFUL;
        }
        return ReturnValues.ReturnRegisterUser.FAILURE;
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
