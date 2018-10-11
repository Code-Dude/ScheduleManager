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
public class Customer {
    private IntegerProperty ID;
    private StringProperty name;
    private IntegerProperty addressID;
    private IntegerProperty active;
    private Timestamp createdDate;
    private StringProperty createdBy;
    private Timestamp lastUpdate;
    private StringProperty lastUpdateBy;
    
    public Customer(int ID, String name, int addressID, int active, 
            Timestamp createdDate, String createdBy, Timestamp lastUpdate, 
            String lastUpdateBy) {
        
        this.ID = new SimpleIntegerProperty(ID);
        this.name = new SimpleStringProperty(name);
        this.addressID = new SimpleIntegerProperty(addressID);
        this.active = new SimpleIntegerProperty(active);
        this.createdDate = createdDate;
        this.createdBy = new SimpleStringProperty(createdBy);
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = new SimpleStringProperty(lastUpdateBy);
    }
    
    public Customer(int ID, String name, int addressID, int active) {
        this.ID = new SimpleIntegerProperty(ID);
        this.name = new SimpleStringProperty(name);
        this.addressID = new SimpleIntegerProperty(addressID);
        this.active = new SimpleIntegerProperty(active);
        modifyLastUpdate("System");
        stampCreation("System");
    }
    
    public Customer() {
    }
    
    public int getCustomerID() {
        return this.ID.get();
    }
    
    public IntegerProperty getCustomerIDProperty() {
        return this.ID;
    }
    
    public void setCustomerID(int customerID) {
        this.ID = new SimpleIntegerProperty(customerID);
    }
    
    public String getCustomerName() {
        return this.name.get();
    }
    
    public StringProperty getCustomerNameProperty() {
        return this.name;
    }
    
    public void setCustomerName(String name) {
        this.name = new SimpleStringProperty(name);
    }
    
    public int getAddressID() {
        return this.addressID.get();
    }
    
    public IntegerProperty getAddressIDProperty() {
        return this.addressID;
    }
    
    public void setAddressID(int addressID) {
        this.addressID = new SimpleIntegerProperty(addressID);
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
    
    public Timestamp getCreatedDate() {
        return this.createdDate;
    }
    
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
    
    public String getCreatedBy() {
        return this.createdBy.get();
    }
    
    public StringProperty getCreatedByProperty() {
        return this.createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = new SimpleStringProperty(createdBy);
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
    
    public StringProperty getLastUpdateByProperty() {
        return this.lastUpdateBy;
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
        
        setCreatedDate(new Timestamp(millis));
        setCreatedBy(userName);
    }
    
    @Override
    public String toString() {
        String customerString = "ID: " + getCustomerID() + " Name: " + getCustomerName() 
                + " Address ID: " + getAddressID() + " Active: " + getActive()
                + " createdDate: " + getCreatedDate() + " Created By: " 
                + getCreatedBy() + " Last Update: " 
                + getLastUpdate() + " Last Update By: " + getLastUpdateBy();
        
        return customerString;
    }
}
