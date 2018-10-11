/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.model;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author scott
 */
public class WeeklyView {
    
    private LocalDateTime dateTime;
    
    private IntegerProperty numAppts;
    
    public WeeklyView(LocalDateTime dateTime, IntegerProperty numAppts) {
        this.dateTime = dateTime;
        this.numAppts = numAppts;
    }
    
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }
    
    public StringProperty getDateAsStringProperty() {
        return new SimpleStringProperty(dateTime.toLocalDate().toString());
    }
    
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    
    public int getNumAppts() {
        return this.numAppts.get();
    }
    
    public IntegerProperty getNumApptsProperty() {
        return this.numAppts;
    }
    
    public void setNumAppts(int numAppts) {
        this.numAppts = new SimpleIntegerProperty(numAppts);
    }
    
    @Override
    public String toString() {
        String dayString = "Date: " + getDateTime() + " Num Appointments: " + getNumAppts();
        
        return dayString;
    }
}
