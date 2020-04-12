package de.simpleprojectmanager.simpleprojectmanager.user;

import de.simpleprojectmanager.simpleprojectmanager.SimpleProjectManager;
import de.simpleprojectmanager.simpleprojectmanager.exception.*;
import de.simpleprojectmanager.simpleprojectmanager.util.EncryptionUtil;

import java.sql.PreparedStatement;
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
    private String passwordRegex = "^(?=.*?[^\\w\\s].*?).{8,}$";

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
        if(!email.matches(this.emailRegex))
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

}
