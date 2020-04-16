package de.simpleprojectmanager.simpleprojectmanager.user;

import de.simpleprojectmanager.simpleprojectmanager.SimpleProjectManager;
import de.simpleprojectmanager.simpleprojectmanager.exception.user.create.*;
import de.simpleprojectmanager.simpleprojectmanager.exception.user.get.UnknownUserGetException;
import de.simpleprojectmanager.simpleprojectmanager.exception.user.get.UserGetException;
import de.simpleprojectmanager.simpleprojectmanager.group.Group;
import de.simpleprojectmanager.simpleprojectmanager.util.EncryptionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.Optional;

public class UserManager {

    //Instance
    public static UserManager instance;

    //Reference to the database connection
    private Connection connection = SimpleProjectManager.getDbCon();

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

        //Checks if the email is already used
        String sql = "SELECT * FROM `user` WHERE UPPER(`email`)=UPPER(?)";

        try(PreparedStatement emailTest = this.connection.prepareStatement(sql)){
            emailTest.setString(1,email);

            //Checks if the email is already used
            if(emailTest.executeQuery().next())
                throw new EmailAlreadyUsedException();
        }catch(Exception e){
            throw new UnknowUserCreateException();
        }

        //Checks if the nickname is already used
        sql = "SELECT * FROM `user` WHERE UPPER(`nickname`)=UPPER(?)";

        try(PreparedStatement nickTest = this.connection.prepareStatement(sql)) {
            nickTest.setString(1,nick);

            //Checks if the email is already used
            if(nickTest.executeQuery().next())
                throw new EmailAlreadyUsedException();
        }catch(Exception e) {
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

        //Inserts the new user
        sql = "INSERT INTO `user` (`nickname`, `passhash`, `passsalt`, `email`, `emailVerified`, `firstname`, `lastname`, `csrfToken`, `sessionToken`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try(PreparedStatement ps = this.connection.prepareStatement(sql)){

            //Creates the variables for the statement
            String[] var = {nick,hash.get(),salt,email,"0",firstname,lastname,csrfToken,sessionToken};

            for (int i = 0; i < var.length; i++)
                ps.setString(i+1,var[i]);

            //Checks if the insertion was successful
            if(ps.executeUpdate()!=1)
                throw new UnknowUserCreateException();
        }catch(Exception e){
            throw new UnknowUserCreateException();
        }
    }

    /**
     * @param email the email
     * @throws UserGetException if the response from the database failes
     * @return an optional user by its email address
     */
    public Optional<User> getUserByEmail(String email) throws UserGetException{

        //Creates the query
        String sql = "SELECT * FROM user WHERE email= ?";

        try(PreparedStatement stm = this.connection.prepareStatement(sql)) {
            stm.setString(1,email);
            ResultSet rs = stm.executeQuery();

            //Checks if the user got found
            if(rs.next())
                return this.getUser(rs);
        } catch(Exception e) {
            throw new UnknownUserGetException();
        }

        return Optional.empty();
    }

    /**
     * Gets a user by his session token and the csrf token
     *
     * @param sessionToken the users session token
     * @param csrfToken the users csrf token
     * @throws UserGetException if the response form the database failes
     * @return the optional user
     */
    public Optional<User> getUserByToken(String sessionToken,String csrfToken) throws UserGetException {
        //Creates the query
        String sql = "SELECT * FROM `user` WHERE `sessionToken`=? AND `csrfToken`=? ";

        try(PreparedStatement ps = this.connection.prepareStatement(sql)){
            ps.setString(1,sessionToken);
            ps.setString(2,csrfToken);

            //Gets the result
            ResultSet rs = ps.executeQuery();

            //Checks if the user got found
            if(rs.next())
                return this.getUser(rs);
        }catch(Exception e){
            throw new UnknownUserGetException();
        }

        return Optional.empty();
    }

    /**
     * @return all users from the group
     * @param group the group
     * @throws UserGetException if the database result failes
     */
    public User[] getUsersFromGroup(Group group) throws UserGetException{
        //Creates the query
        String sql = "SELECT * FROM `user_group` JOIN `user` ON `user_group`.`userID`=`user`.`id` WHERE `user_group`.`groupID` = ?;";

        try(PreparedStatement ps = this.connection.prepareStatement(sql)){
            ps.setInt(1,group.getId());

            //Gets the result
            try(ResultSet rs = ps.executeQuery()){
                LinkedList<User> users = new LinkedList<>();

                while(rs.next())
                    users.add(this.getUser(rs).get());

                //Returns the found users as an array
                return users.toArray(new User[users.size()]);
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new UnknownUserGetException();
        }
    }

    /**
     * @param result the result-set
     * @return the optional user
     */
    private Optional<User> getUser(ResultSet result){
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
