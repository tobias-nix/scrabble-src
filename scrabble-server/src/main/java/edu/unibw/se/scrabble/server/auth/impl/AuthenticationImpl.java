package edu.unibw.se.scrabble.server.auth.impl;

import edu.unibw.se.scrabble.common.base.ReturnValues;
import edu.unibw.se.scrabble.server.auth.Authentication;
import edu.unibw.se.scrabble.server.auth.Credentials;
import edu.unibw.se.scrabble.server.data.AuthData;

import java.util.Objects;
import java.util.regex.Pattern;

public class AuthenticationImpl implements Authentication, Credentials {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9]{4,15}");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*?[A-Za-z])(?=.*?[0-9])(?=.*?[?!$%&/()*]).{8,20}$");

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
            return ReturnValues.ReturnLoginUser.FAILURE;
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return ReturnValues.ReturnLoginUser.INVALID_USERNAME;
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return ReturnValues.ReturnLoginUser.INVALID_PASSWORD;
        }
        if (!authData.usernameExists(username)) {
            return ReturnValues.ReturnLoginUser.USERNAME_NOT_IN_DATABASE;
        }
        if (!Objects.equals(authData.getPassword(username), password)) {
            return ReturnValues.ReturnLoginUser.WRONG_PASSWORD;
        }
        return ReturnValues.ReturnLoginUser.SUCCESSFUL;
    }

    @Override
    public ReturnValues.ReturnRegisterUser registerUser(String username, String password) {
        if (username == null || password == null) {
            return ReturnValues.ReturnRegisterUser.FAILURE;
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return ReturnValues.ReturnRegisterUser.INVALID_USERNAME;
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return ReturnValues.ReturnRegisterUser.INVALID_PASSWORD;
        }
        if (authData.usernameExists(username)) {
            return ReturnValues.ReturnRegisterUser.USERNAME_ALREADY_EXISTS;
        }
        if (authData.createUser(username, password)) {
            return ReturnValues.ReturnRegisterUser.SUCCESSFUL;
        }
        return ReturnValues.ReturnRegisterUser.DATABASE_FAILURE;
    }
}
