package com.finalAPI.farmerLoginSignupApi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "token")
public class Token  {
     @Id
    private String id;
    private String token;
    private boolean loggedOut;
    @DBRef
    private User user;

    public Token() {
    }

    public Token(String id, String token, User user, boolean loggedOut) {
        this.id = id;
        this.token = token;
        this.user = user;
        this.loggedOut = loggedOut;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLoggedOut() {
        return loggedOut;
    }

    public void setLoggedOut(boolean loggedOut) {
        this.loggedOut = loggedOut;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
