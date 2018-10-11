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
public class City {
    private IntegerProperty cityID;
    private StringProperty city;
    private IntegerProperty countryID;
    private Timestamp createDate;
    private StringProperty createdBy;
    private Timestamp lastUpdate;
    private StringProperty lastUpdateBy;
    
    public City(int cityID, String city, int countryID, Timestamp createDate,
                String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        
        this.cityID = new SimpleIntegerProperty(cityID);
        this.city = new SimpleStringProperty(city);
        this.countryID = new SimpleIntegerProperty(countryID);
        this.createDate = createDate;
        this.createdBy = new SimpleStringProperty(createdBy);
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = new SimpleStringProperty(lastUpdateBy);
    }
    
    public City(int cityID, String city, int countryID, String userName) {
        this.cityID = new SimpleIntegerProperty(cityID);
        this.city = new SimpleStringProperty(city);
        this.countryID = new SimpleIntegerProperty(countryID);
        stampCreation(userName);
        modifyLastUpdate(userName);
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
    
    public String getCity() {
        return this.city.get();
    }
    
    public StringProperty getCityProperty() {
        return this.city;
    }
    
    public void setCity(String city) {
        this.city = new SimpleStringProperty(city);
    }
    
    public int getCountryID() {
        return this.countryID.get();
    }
    
    public IntegerProperty getCountryIDProperty() {
        return this.countryID;
    }
    
    public void setCountryID(int countryID) {
        this.countryID = new SimpleIntegerProperty(countryID);
    }
    
    public Timestamp getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
    
    public String getCreatedBy() {
        return this.createdBy.get();
    }
    
    public StringProperty getCreatedByProperty() {
        return this.createdBy;
    }
    
    public void setCreatedBy(String userName) {
        this.createdBy = new SimpleStringProperty(userName);
    }
    
    public Timestamp getLastUpdate() {
        return this.lastUpdate;
    }
    
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    public String getLastUpdatedBy() {
        return this.lastUpdateBy.get();
    }
    
    public StringProperty getLastUpdateByProperty() {
        return this.lastUpdateBy;
    }
    
    public void setLastUpdateBy(String userName) {
        this.lastUpdateBy = new SimpleStringProperty(userName);
    }
    
    public void modifyLastUpdate(String userName) {
        Instant instant = Instant.now();
        long millis = instant.toEpochMilli();
        setLastUpdate(new Timestamp(millis));
        setLastUpdateBy(userName);
    }
    
    public void stampCreation(String userName) {
        Instant instant = Instant.now();
        long millis = instant.toEpochMilli();
        
        setCreateDate(new Timestamp(millis));
        setCreatedBy(userName);
    }
    
    @Override
    public String toString() {
        return this.getCity() + " " + this.getCityID() + " " + this.getCountryID();
    }
}
