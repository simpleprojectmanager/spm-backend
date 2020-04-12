package de.simpleprojectmanager.simpleprojectmanager.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestUser {

    private String email;
    private String password;

    public RequestUser(@JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
