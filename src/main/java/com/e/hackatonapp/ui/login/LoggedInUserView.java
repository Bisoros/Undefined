package com.e.hackatonapp.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String name;
    private String token;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String token, String name) {
        this.name = name;
        this.token = token;
    }
    String getToken(){return token;}
    String getName() {
        return name;
    }
}
