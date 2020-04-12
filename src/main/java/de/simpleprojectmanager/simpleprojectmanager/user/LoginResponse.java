package de.simpleprojectmanager.simpleprojectmanager.user;


public class LoginResponse {

    private String nickname;
    private String firstname;
    private String lastname;
    private String csrfToken;
    private String sessionToken;

    public LoginResponse(User user) {

        this.nickname = user.getNickname();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.csrfToken = user.getCsrfToken();
        this.sessionToken = user.getSessionToken();

    }

    public String getNickname() {
        return nickname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

}
