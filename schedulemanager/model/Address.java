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
public class Address {
    private IntegerProperty addressID;
    private StringProperty address;
    private StringProperty address2;
    private IntegerProperty cityID;
    private StringProperty postalCode;
    private StringProperty phoneNumber;
    private Timestamp createDate;
    private StringProperty createdBy;
    private Timestamp lastUpdate;
    private StringProperty lastUpdatedBy;
    
    public Address(int addressID, String address, String address2, int cityID,
                   String postalCode, String phoneNumber, Timestamp createDate,
                   String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        
        this.addressID = new SimpleIntegerProperty(addressID);
        this.address = new SimpleStringProperty(address);
        this.address2 = new SimpleStringProperty(address2);
        this.cityID = new SimpleIntegerProperty(cityID);
        this.postalCode = new SimpleStringProperty(postalCode);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.createDate = createDate;
        this.createdBy = new SimpleStringProperty(createdBy);
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = new SimpleStringProperty(lastUpdateBy);
    }
    
    public Address(int addressID, String address, String address2, int cityID,
                  String postalCode, String phone, String userName) {
        
        this.addressID = new SimpleIntegerProperty(addressID);
        this.address = new SimpleStringProperty(address);
        this.address2 = new SimpleStringProperty(address2);
        this.cityID = new SimpleIntegerProperty(cityID);
        this.postalCode = new SimpleStringProperty(postalCode);
        this.phoneNumber = new SimpleStringProperty(phone);
        stampCreation(userName);
        modifyLastUpdate(userName);
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
    
    public String getAddress() {
        return this.address.get();
    }
    
    public StringProperty getAddressProperty() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = new SimpleStringProperty(address);
    }
    
    public String getAddress2() {
        return this.address2.get();
    }
    
    public StringProperty getAddress2Property() {
        return this.address2;
    }
    
    public void setAddress2(String address) {
        this.address2 = new SimpleStringProperty(address);
    }    
    
    public int getCityID() {
        return this.cityID.get();
    }
    
    public IntegerProperty getCityIDProperty() {
        return this.cityID;
    }
    
    public void setCityID(int cityID) {
        this.cityID = new SimpleIntegerProperty(cityID);
    }
    
    public String getPostalCode() {
        return this.postalCode.get();
    }
    
    public StringProperty getPostalCodeProperty() {
        return this.postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = new SimpleStringProperty(postalCode);
    }
    
    public String getPhoneNumber() {
        return this.phoneNumber.get();
    }
    
    public StringProperty getPhoneNumberProperty() {
        return this.phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
    }
    
    public Timestamp getCreatedDate() {
        return this.createDate;
    }
    
    public void setCreatedDate(Timestamp createdDate) {
        this.createDate = createdDate;
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
        return this.lastUpdatedBy.get();
    }
    
    public StringProperty getLastUpdateByProperty() {
        return this.lastUpdatedBy;
    }
    
    public void setLastUpdateBy(String userName) {
        this.lastUpdatedBy = new SimpleStringProperty(userName);
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
        String addressString = "Address ID: " + getAddressID() + " Address: "
                + getAddress() + " Address 2: " + getAddress2() + " City ID: "
                + getCityID() + " Postal Code: " + getPostalCode() + " Phone Number: "
                + getPhoneNumber() + " Created Date: " + getCreatedDate()
                + " Created By: " + getCreatedBy() + " Last Update " + getLastUpdate()
                + " Last Update By: " + getLastUpdateBy();
        
       return addressString;
    }
}
