/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.stream.Stream;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author scott
 */
public class ClockLister {
    
    public static ObservableList<LocalTime> hourlyClock;
    
    static {
        hourlyClock = FXCollections.observableArrayList();
        
        for(int i = 0; i < 24; ++i) {
            for(int j = 0; j <= 59; j += 15) {
                LocalTime time = LocalTime.of(i, j);
            
                hourlyClock.add(time);
            }
        }
    }
    
    public static ObservableList<LocalTime> getClock() {
        return hourlyClock;
    }
    
    //This is an example of a Lambda Expression, this lambda allowed my to filter
    // times in this hourly clock to get the time asked for in a parallel fashion
    // as to speed up excecution.
    public static int indexOfTime(LocalTime time) {
        Stream<LocalTime> localTimes = hourlyClock.stream().unordered().parallel();
        Optional<LocalTime> optTime = localTimes.filter(x -> x.equals(time)).findFirst();
        
        return hourlyClock.indexOf(optTime.get());
    }
    
    public static ObservableList<LocalDateTime> getWeek(LocalDateTime dateTime) {
        ObservableList<LocalDateTime> week = FXCollections.observableArrayList();
        for(int i = 1; i < 8; ++i) {
            if(i < dateTime.getDayOfWeek().getValue()) {
                LocalDateTime weekDay = dateTime.minusDays(dateTime.getDayOfWeek().getValue() - i);
                week.add(weekDay);
            }
            else if(i > dateTime.getDayOfWeek().getValue()) {
                LocalDateTime weekDay = dateTime.plusDays(i - dateTime.getDayOfWeek().getValue());
                week.add(weekDay);
            }
            else {
                week.add(dateTime);
            }
        }
        
        return week;
    }
    
    public static StringProperty getDateTimeAsStringProperty(LocalDateTime dateTime) {
        StringProperty dateTimeString = new SimpleStringProperty(dateTime.toString());
        
        return dateTimeString;
    }
    
    public static StringProperty getDateAsStringProperty(LocalDateTime dateTime) {
        StringProperty dateString = new SimpleStringProperty(dateTime.toLocalDate().toString());
        
        return dateString;
    }
}
