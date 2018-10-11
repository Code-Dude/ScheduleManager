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
public class Country {
    private IntegerProperty countryID;
    private StringProperty country;
    private Timestamp createDate;
    private StringProperty createdBy;
    private Timestamp lastUpdate;
    private StringProperty lastUpdateBy;
    
    public Country(int countryID, String country, Timestamp createDate, 
                   String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        
        this.countryID = new SimpleIntegerProperty(countryID);
        this.country = new SimpleStringProperty(country);
        this.createDate = createDate;
        this.createdBy = new SimpleStringProperty(createdBy);
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = new SimpleStringProperty(lastUpdateBy);
    }
    
    public Country(int countryID, String country, String userName) {
        this.countryID = new SimpleIntegerProperty(countryID);
        this.country = new SimpleStringProperty(country);
        stampCreation(userName);
        modifyLastUpdate(userName);
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
    
    public String getCountry() {
        return this.country.get();
    }
    
    public StringProperty getCountryProperty() {
        return this.country;
    }
    
    public void setCountry(String country) {
        this.country = new SimpleStringProperty(country);
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
    
    private void stampCreation(String userName) {
        Instant instant = Instant.now();
        long millis = instant.toEpochMilli();
        
        setCreatedDate(new Timestamp(millis));
        setCreatedBy(userName);
    }
}
