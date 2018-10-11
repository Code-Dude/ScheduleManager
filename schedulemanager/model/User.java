/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.model;

import java.sql.Timestamp;
import java.time.Instant;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author scott
 */
public class User {
    private final IntegerProperty userID;
    private final StringProperty userName;
    private StringProperty password;
    private IntegerProperty active;
    private Timestamp createDate;
    private StringProperty createBy;
    private Timestamp lastUpdate;
    private StringProperty lastUpdateBy;
    
    public User(int userID, String userName, String password, int active,
                Timestamp createDate, String createBy, Timestamp lastUpdate,
                String lastUpdateBy) {
        
        this.userID = new SimpleIntegerProperty(userID);
        this.userName = new SimpleStringProperty(userName);
        this.password = new SimpleStringProperty(password);
        this.active = new SimpleIntegerProperty(active);
        this.createDate = createDate;
        this.createBy = new SimpleStringProperty(createBy);
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = new SimpleStringProperty(lastUpdateBy);
    }
    
    public User(int userID, String userName, String password, int active, String createUserName) {
        this.userID = new SimpleIntegerProperty(userID);
        this.userName = new SimpleStringProperty(userName);
        this.password = new SimpleStringProperty(password);
        this.active = new SimpleIntegerProperty(active);
        stampCreation(createUserName);
        modifyLastUpdate(createUserName);
    }
    
    public int getUserID() {
        return this.userID.get();
    }
    
    public IntegerProperty getUserIDProperty() {
        return this.userID;
    }
    
    public String getUserName() {
        return this.userName.get();
    }
    
    public StringProperty getUserNameProperty() {
        return this.userName;
    }
    
    public String getPassword() {
        return this.password.get();
    }
    
    public void setPassword(String password) {
        this.password = new SimpleStringProperty(password);
    }
    
    public int getActive() {
        return this.active.get();
    }
    
    public IntegerProperty getActiveProperty() {
        return this.active;
    }
    
    public void setActive(int active) {
        this.active = new SimpleIntegerProperty(active);
    }
    
    public Timestamp getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
    
    public String getCreateBy() {
        return this.createBy.get();
    }
    
    public StringProperty getCreateByProperty() {
        return this.createBy;
    }
    
    public void setCreateBy(String createBy) {
        this.createBy = new SimpleStringProperty(createBy);
    }
    
    public Timestamp getLastUpdate() {
        return this.lastUpdate;
    }
    
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    public String getLastUpdateBy() {
        return this.lastUpdateBy.get();
    }
    
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = new SimpleStringProperty(lastUpdateBy);
    }
    
    public void modifyLastUpdate(String userName) {
        Instant instant = Instant.now();
        long millis = instant.toEpochMilli();
        setLastUpdate(new Timestamp(millis));
        setLastUpdateBy(userName);
    }
    
    private void stampCreation(String userName) {
        Instant instant = Instant.now();
        long millis = instant.toEpochMilli();
        
        setCreateDate(new Timestamp(millis));
        setCreateBy(userName);
    }
    
    @Override
    public String toString() {
        String userString = "User ID: " + getUserID() + " UserName: "
                + getUserName() + " Active: " + getActive() + " Create Date: "
                + getCreateDate() + " Created By: " + " Last Update: " 
                + getLastUpdate() + " Last Update By: " + getLastUpdateBy();
        
        return userString;
    }
}
