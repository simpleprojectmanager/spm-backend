package de.simpleprojectmanager.simpleprojectmanager.group;

import de.simpleprojectmanager.simpleprojectmanager.SimpleProjectManager;
import de.simpleprojectmanager.simpleprojectmanager.exception.group.create.*;
import de.simpleprojectmanager.simpleprojectmanager.exception.group.get.GroupGetException;
import de.simpleprojectmanager.simpleprojectmanager.exception.group.get.UnknownGroupGetException;
import de.simpleprojectmanager.simpleprojectmanager.exception.group.insert.GroupInsertException;
import de.simpleprojectmanager.simpleprojectmanager.exception.group.insert.UnknownInsertException;
import de.simpleprojectmanager.simpleprojectmanager.exception.group.insert.UserAlreadyInGroupException;
import de.simpleprojectmanager.simpleprojectmanager.permissions.EnumGlobalPermissions;
import de.simpleprojectmanager.simpleprojectmanager.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.Optional;

public class GroupManager {

    private static GroupManager instance;

    //Reference to the database connection
    private Connection connection = SimpleProjectManager.getDbCon();

    //Name checking regex
    private String nameRegex = "^.{4,20}$";

    private GroupManager(){}

    public static GroupManager getInstance(){
        if(instance==null)
            instance=new GroupManager();
        return instance;
    }

    /**
     * Creates a group with the given parameters
     *
     * @param name the group's name
     * @param permissions the permission-number
     * @param color the group's color
     * @param hierarchy the heraical step of the group
     * @throws GroupCreateException if anything goes wrong or anything wrong was given
     */
    public void createGroup(String name,long permissions,int color,int hierarchy) throws GroupCreateException {
        //Checks if the name is valid
        if(!name.matches(this.nameRegex))
            throw new InvalidNameException();

        //Checks if the permissions are valid
        if(permissions < 0 || permissions > EnumGlobalPermissions.getAllPermissions())
            throw new InvalidPermissionsException();

        //Checks if the color is valid
        if(color < 0 || color > 0xFFFFFF)
            throw new InvalidColorException();

        //Checks if the group`s name is in use
        String sql = "SELECT * FROM `group` WHERE UPPER(`name`)=UPPER(?);";

        try(PreparedStatement nameTest = this.connection.prepareStatement(sql)){
            nameTest.setString(1,name);

            //Checks if the name is used
            if(nameTest.executeQuery().next())
                throw new NameAlreadyUsedException();
        }catch(Exception e){
            throw new UnknownGroupCreateException();
        }

        //Creates the query
        sql =  "INSERT INTO `group` (`name`, `permissions`, `color`, `hierarchy`) VALUES (?,?,?,?);";

        try(PreparedStatement ps = this.connection.prepareStatement(sql)){
            ps.setString(1,name);
            ps.setLong(2,permissions);
            ps.setInt(3,color);
            ps.setInt(4,hierarchy);

            //Checks if the insert wasn't successful
            if(ps.executeUpdate()!=1)
                throw new Exception();
        }catch(Exception e){
            throw new UnknownGroupCreateException();
        }
    }

    /**
     * @param name the name of the group
     * @return optionally the group by its name
     * @throws GroupGetException if the database response failes
     */
    public Optional<Group> getGroupByName(String name) throws GroupGetException{
        //Creates the query
        String sql = "SELECT * FROM `group` WHERE UPPER(`name`) = UPPER(?);";

        try(PreparedStatement ps = this.connection.prepareStatement(sql)){
            ps.setString(1,name);

            ResultSet rs = ps.executeQuery();

            //Checks if the group got found
            if(rs.next())
                return this.getGroup(rs);
        }catch(Exception e){
            throw new UnknownGroupGetException();
        }

        return Optional.empty();
    }

    /**
     * @param user the user from which the groups should be searched
     * @throws GroupGetException if the response from the database failes
     * @return the optional groups in which the user is
     */
    public Group[] getGroupsFromUser(User user) throws GroupGetException {
        //Creates the query
        String sql = "SELECT * FROM `user_group` JOIN `group` ON `user_group`.`groupID`=`group`.`id` WHERE `user_group`.`userID`=?";

        try(PreparedStatement ps = this.connection.prepareStatement(sql)){
            ps.setInt(1,user.getId());

            //Gets the result
            try(ResultSet rs = ps.executeQuery()){
                LinkedList<Group> groups = new LinkedList<>();

                while(rs.next())
                    groups.add(this.getGroup(rs).get());

                //Returns the converted array
                return groups.toArray(new Group[groups.size()]);
            }
        }catch(Exception e){
            throw new UnknownGroupGetException();
        }
    }

    /**
     * Add the given user to the given group
     * @param user the user
     * @param group the group
     * @throws GroupInsertException if the user is already part of that group
     */
    public void addUserToGroup(User user,Group group) throws GroupInsertException {
        //Creates the query
        String sql = "INSERT INTO `user_group`(userid,groupid) SELECT ?,? WHERE NOT EXISTS(SELECT 1 FROM `user_group` WHERE userid = ? AND groupid = ?);";

        try(PreparedStatement ps = this.connection.prepareStatement(sql)){
            ps.setInt(1,user.getId());
            ps.setInt(2,group.getId());
            ps.setInt(3,user.getId());
            ps.setInt(4,group.getId());

            //Checks if the user is already in that group
            if(ps.executeUpdate()!=1)
                throw new UserAlreadyInGroupException();
        }catch(Exception e){
            e.printStackTrace();
            throw new UnknownInsertException();
        }
    }

    /**
     * @param resultSet the given result
     * @return the optional group
     */
    private Optional<Group> getGroup(ResultSet resultSet){
        try {
            return Optional.of(new Group(
               resultSet.getInt("id"),
               resultSet.getString("name"),
               resultSet.getInt("permissions"),
               resultSet.getInt("color")  ,
               resultSet.getInt("hierarchy")
            ));
        }catch(Exception e){
            return Optional.empty();
        }
    }
}
