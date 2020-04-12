package de.simpleprojectmanager.simpleprojectmanager.user;

public class User {

    private int id;
    private String nickname;
    private String passhash;
    private String passsalt;
    private String email;
    private boolean emailVerified;
    private String firstname, lastname;
    private String emailResetToken;
    private String csrfToken;
    private String sessionToken;

    public User(int id, String nickname, String passhash, String passsalt, String email, boolean emailVerified, String firstname, String lastname, String emailResetToken, String csrfToken, String sessionToken) {
        this.id = id;
        this.nickname = nickname;
        this.passhash = passhash;
        this.passsalt = passsalt;
        this.email = email;
        this.emailVerified = emailVerified;
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailResetToken = emailResetToken;
        this.csrfToken = csrfToken;
        this.sessionToken = sessionToken;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPasshash() {
        return passhash;
    }

    public String getPasssalt() {
        return passsalt;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmailResetToken() {
        return emailResetToken;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
