package de.simpleprojectmanager.simpleprojectmanager.api;

import de.simpleprojectmanager.simpleprojectmanager.SimpleProjectManager;
import de.simpleprojectmanager.simpleprojectmanager.api.response.ErrorResponse;
import de.simpleprojectmanager.simpleprojectmanager.api.response.IDefaultResponse;
import de.simpleprojectmanager.simpleprojectmanager.user.LoginResponse;
import de.simpleprojectmanager.simpleprojectmanager.user.RequestUser;
import de.simpleprojectmanager.simpleprojectmanager.user.User;
import de.simpleprojectmanager.simpleprojectmanager.user.UserManager;
import de.simpleprojectmanager.simpleprojectmanager.util.EncryptionUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.util.Optional;

@RestController
public class WebAPI {

    //establishes an active connection to the database
    private Connection con = SimpleProjectManager.getDbCon();

    /**
     * Waits for a post json request to "/api/login" with attributes "email" and "password" and
     * compares the database password hash with the result of the hash from the json password and database salt.
     *
     * @param user converts a given json object with attributes "email" and "password" into user object
     * @return return true if the login was successful
     */



    @PostMapping("api/login")
    public IDefaultResponse login(@RequestBody RequestUser user) {

        //gets the email and password value from the given user
        String email = user.getEmail();
        String password = user.getPassword();

        try {

            /*
            executes an sql command and returns the password hash and the password salt for a specified
            email address from a given database
             */

            Optional<User> receivedOptionalUser = UserManager.getInstance().getUserByEmail(email);

            /*
            gets the hash of the given user password combined with its database stored salt (password+salt)
            and compares the result with the database password hash
             */

            if(receivedOptionalUser.isPresent()) {
                User receivedUser = receivedOptionalUser.get();
                Optional<String> hash = EncryptionUtil.getInstance().hashSHA512(password +receivedUser.getPasssalt());
                if(hash.isEmpty())
                    throw new Exception("Hash-failed");

                if(hash.get().equals(receivedUser.getPasshash()))
                    return new LoginResponse(receivedUser);
            }

        } catch(Exception e) {}

        //Exits with an error-response
        return new ErrorResponse("user.login.failed");
    }
}
