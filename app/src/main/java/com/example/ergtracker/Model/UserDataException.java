package com.example.ergtracker.Model;

import android.service.autofill.UserData;

class UserDataException extends Exception{
    public UserDataException(String errorMessage) {
        super(errorMessage);
    }

}
