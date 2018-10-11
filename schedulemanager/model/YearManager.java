/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.model;

import java.time.LocalDateTime;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;

/**
 *
 * @author scott
 */
public class YearManager {
    private ArrayList<WeeklyView> year;
    
    private AppointmentManager appAppointmentManager;
    
    public YearManager(AppointmentManager appAppointmentManager) {
        year = new ArrayList<>();
        
        this.appAppointmentManager = appAppointmentManager;
    }
    
    public ArrayList<WeeklyView> getYear(LocalDateTime dateTime) {
        LocalDateTime firstDayOfYear = dateTime.with(firstDayOfYear());
        for(int i = 0; i < 365; ++i) {
            LocalDateTime date = firstDayOfYear.plusDays(i);
            IntegerProperty numAppts = appAppointmentManager.getNumAppointmentsOnDay(date);
            WeeklyView day = new WeeklyView(date, numAppts);
            
            year.add(day);
        }
        
        return year;
    }
}
