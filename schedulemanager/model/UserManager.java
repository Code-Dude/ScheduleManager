/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

/**
 *
 * @author scott
 */
public class UserManager {
    private ArrayList<User> users;
    
    public UserManager() {
        this.users = new ArrayList<>();
    }
    
    public void addUser(Connection conn, User user) throws SQLException {
        users.add(user);
        
        String userInsertSQL = "insert into user "
                + "(userId, userName, password, active, createDate, createBy, "
                + "lastUpdate, lastUpdatedBy) values(?,?,?,?,?,?,?,?)";
        
        PreparedStatement prepStatement = conn.prepareStatement(userInsertSQL);
        
        prepStatement.setInt(1, user.getUserID());
        prepStatement.setString(2, user.getUserName());
        prepStatement.setString(3, user.getPassword());
        prepStatement.setInt(4, user.getActive());
        prepStatement.setTimestamp(5, user.getCreateDate());
        prepStatement.setString(6, user.getCreateBy());
        prepStatement.setTimestamp(7, user.getLastUpdate());
        prepStatement.setString(8, user.getLastUpdateBy());
        
        prepStatement.executeUpdate();
    }
    
    public void loadUsers(Connection conn) throws SQLException {
       Statement statement = conn.createStatement();
       ResultSet rs = statement.executeQuery("select * from user");
       while(rs.next()) {
           int userID = rs.getInt("userId");
           String userName = rs.getString("userName");
           String password = rs.getString("password");
           int active = rs.getInt("active");
           Timestamp createDate = rs.getTimestamp("createDate");
           String createBy = rs.getString("createBy");
           Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
           String lastUpdateBy = rs.getString("lastUpdatedBy");
           
           User user = new User(userID, userName, password, active, createDate,
                                createBy, lastUpdate, lastUpdateBy);
           
           users.add(user);
       }        
    }
    
    public void deleteUser(Connection conn, User user) throws SQLException {
        String userDeleteSQL = "delete from user where userId = ?";
        PreparedStatement prepStatement = conn.prepareStatement(userDeleteSQL);
        
        prepStatement.setInt(1, user.getUserID());
        
        prepStatement.executeUpdate();
        users.remove(user);
    }
    
    public User getUser(int userID) {
        Stream<User> userStream = users.stream().unordered().parallel();
        Optional<User> user = userStream.filter(x -> x.getUserID() == userID).findFirst();
        
        return user.get();
    }
    
    public User getUser(String userName) {
        Stream<User> userStream = users.stream().unordered().parallel();
        Optional<User> user = userStream.filter(x -> x.getUserName().equals(userName)).findFirst();
        
        return user.get();
    }
    
    public void printUsers() {
        for(User user : users) {
            System.out.println(user);
        }
    }
    
    public boolean checkPassword(String userName, String password) throws InvalidCredentialsException {
        User user = getUser(userName);
        if(user != null) {
            if(user.getPassword().equals(password)) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            throw new InvalidCredentialsException();
        }
    }
    
    public int autoGenID() {
        int newID = 1;
        for(User user : users) {
            if(user.getUserID() >= newID) {
                newID = user.getUserID() + 1;
            }
        }

        return newID;
    }
    
    public ArrayList<User> getUsers() {
        return this.users;
    }
}
