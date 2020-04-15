package de.simpleprojectmanager.simpleprojectmanager.user;

import de.simpleprojectmanager.simpleprojectmanager.SimpleProjectManager;
import de.simpleprojectmanager.simpleprojectmanager.exception.*;
import de.simpleprojectmanager.simpleprojectmanager.util.EncryptionUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserManager {

    //Instance
    public static UserManager instance;

    private UserManager() {
        instance=this;
    }

    public static UserManager getInstance() {
        //Checks if the instance has not been created
        if(instance==null)
            instance=new UserManager();
        return instance;
    }

    //Email verifier
    private String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    //Password verifier
    private String passwordRegex = "^(?=.*?[^\\w\\s].*?).{8,20}$";

    //First and last-name verifier
    private String nameRegex = "^\\S{2,20}$";

    //Nickname verifier
    private String nickRegex = "^\\S{4,20}$";

    /**
     * Creates the user in the database
     *
     * @param nick the user's nickname
     * @param firstname the user's firstname
     * @param lastname the user's lastname
     * @param email the user's email
     * @param password the user's password
     * */
    public void createUser(String nick,String firstname,String lastname, String email,String password) throws UserCreateException {
        //Checks if the firstName is valid
        if(!firstname.matches(this.nameRegex))
            throw new InvalidFirstNameException();

        //Checks if the lastName is valid
        if(!lastname.matches(this.nameRegex))
            throw new InvalidLastNameException();

        //Checks if the nickName is valid
        if(!nick.matches(this.nickRegex))
            throw new InvalidNickNameException();

        //Checks if the password is valid
        if(!password.matches(this.passwordRegex))
            throw new InvalidPasswordException();

        //Checks if the password is valid
        if(!email.matches(this.emailRegex) || email.length()>40)
            throw new InvalidEmailException();

        try{
            //Creates the email test
            PreparedStatement emailTest = SimpleProjectManager.getDbCon().prepareStatement("SELECT * FROM `user` WHERE `email`=?");
            //Sets the email
            emailTest.setString(1,email);

            //Checks if the email is already used
            if(emailTest.executeQuery().next())
                throw new EmailAlreadyUsedException();

            //Creates the nickname test
            PreparedStatement nickTest = SimpleProjectManager.getDbCon().prepareStatement("SELECT * FROM `user` WHERE `nickname`=?");
            //Sets the email
            nickTest.setString(1,nick);

            //Checks if the email is already used
            if(nickTest.executeQuery().next())
                throw new NickAlreadyUsedException();
        }catch(SQLException e){
            throw new UnknowUserCreateException();
        }

        //Generates the salt
        var salt = EncryptionUtil.getInstance().genRandomAscii(10);

        //Generates the hash
        Optional<String> hash = EncryptionUtil.getInstance().hashSHA512(password+salt);

        //Checks if the hash was successful
        if(hash.isEmpty())
            throw new UnknowUserCreateException();

        //Generates the sessionToken
        String sessionToken = EncryptionUtil.getInstance().genRandomAscii(30);

        //Generates the csrf token
        String csrfToken = EncryptionUtil.getInstance().genRandomAscii(10);

        try {
            //Creates the prepared statement
            PreparedStatement ps = SimpleProjectManager.getDbCon().prepareStatement("INSERT INTO `user` (`nickname`, `passhash`, `passsalt`, `email`, `emailVerified`, `firstname`, `lastname`, `csrfToken`, `sessionToken`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");

            //Creates the variables for the statement
            String[] var = {nick,hash.get(),salt,email,"0",firstname,lastname,csrfToken,sessionToken};

            //Iterates over the variables
            for (int i = 0; i < var.length; i++)
                ps.setString(i+1,var[i]);

            //Executes the statement
            ps.executeUpdate();
        }catch(Exception e){
            throw new UnknowUserCreateException();
        }
    }

    public Optional<User> getUserByEmail(String email) {

        try {

            PreparedStatement stm = SimpleProjectManager.getDbCon().prepareStatement("SELECT * FROM user WHERE email= ?");
            stm.setString(1,email);
            ResultSet rs = stm.executeQuery();

            //Checks if the user got found
            if(rs.next())
                //Returns the formatted user
                return this.getFormattedUser(rs);
        } catch(Exception e) {
            return Optional.empty();
        }

        return null;
    }

    /**
     * Gets a user by his session token and the csrf token
     *
     * @param sessionToken the users session token
     * @param csrfToken the users csrf token
     * @return the optional user
     */
    public Optional<User> getUserByToken(String sessionToken,String csrfToken){
        try{
            //Creates the statement
            PreparedStatement ps = SimpleProjectManager.getDbCon().prepareStatement("SELECT * FROM `user` WHERE `sessionToken`=? AND `csrfToken`=? ");
            //Appends the values
            ps.setString(1,sessionToken);
            ps.setString(2,csrfToken);

            //Gets the result
            ResultSet rs = ps.executeQuery();

            //Checks if the user got found
            if(rs.next())
                //Returns the formatted user
                return this.getFormattedUser(rs);
        }catch(Exception e){}
        return Optional.empty();
    }

    /**
     * Returns a formatted user from a given result set
     * @param result the resultset
     * @return the optional user
     */
    private Optional<User> getFormattedUser(ResultSet result){
        try {
            //Gets the formatted user
            return Optional.of(new User(
                    result.getInt("id"),
                    result.getString("nickname"),
                    result.getString("passhash"),
                    result.getString("passsalt"),
                    result.getString("email"),
                    result.getBoolean("emailVerified"),
                    result.getString("firstname"),
                    result.getString("lastname"),
                    result.getString("emailResetToken"),
                    result.getString("csrfToken"),
                    result.getString("sessionToken")
            ));
        }catch (Exception e){
            return Optional.empty();
        }

    }
}
