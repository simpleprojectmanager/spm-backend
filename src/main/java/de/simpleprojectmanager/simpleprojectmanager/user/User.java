package de.simpleprojectmanager.simpleprojectmanager.user;

import de.simpleprojectmanager.simpleprojectmanager.SimpleProjectManager;
import de.simpleprojectmanager.simpleprojectmanager.util.EncryptionUtil;

import java.sql.PreparedStatement;

public class User {

    private final int id;
    private final String nickname;
    private final String passhash;
    private final String passsalt;
    private String email;
    private boolean emailVerified;
    private final String firstname, lastname;
    private String emailResetToken;
    private String csrfToken;
    private final String sessionToken;

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

    /**
     * Regenerates the csrf token
     */
    public void regenerateCSRFToken(){
        this.csrfToken=EncryptionUtil.getInstance().genRandomAscii(10);
    }

    /**
     * Pushes the user back to the database
     * @return if an error occurred
     */
    public boolean pushPack(){
        try{
            //Creates the statement
            PreparedStatement ps = SimpleProjectManager.getDbCon().prepareStatement("UPDATE `user` SET `email`=?, `emailVerified`=?,`emailResetToken`=?, `csrfToken`=?,`sessionToken`=? WHERE `id`=?");

            //Appends the values
            ps.setString(1,this.getEmail());
            ps.setBoolean(2,this.isEmailVerified());
            ps.setString(3,this.getEmailResetToken());
            ps.setString(4,this.getCsrfToken());
            ps.setString(5,this.getSessionToken());
            ps.setInt(6,this.getId());

            //Executes the query
            return ps.executeUpdate()==1;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
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
