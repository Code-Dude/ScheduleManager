/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.view_controller;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import resources.ClockLister;
import schedulemanager.ScheduleManager;
import schedulemanager.model.Appointment;
import schedulemanager.model.Customer;

/**
 * FXML Controller class
 *
 * @author scott
 */
public class AddAppointmentController implements Initializable {
   
    @FXML
    private ListView<LocalTime> startTimeList;

    @FXML
    private TextField locationField;

    @FXML
    private TextField titleField;

    @FXML
    private DatePicker apptDatePicker;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField contactField;

    @FXML
    private TextField urlField;
    
    @FXML
    private TableColumn<Customer, Integer> apptCustomerIDColumn;
    
    @FXML
    private TableColumn<Customer, String> apptCustomerNameColumn;

    @FXML
    private TableView<Customer> apptCustomerTable;

    @FXML
    private ListView<LocalTime> endTimeList;
    
    private ScheduleManager mainApp;
    
    private Stage dialogStage;
    
    private Customer customer;
    
    private Appointment appointment;
    
    private boolean saveClicked;
    
    private String localizedErrorMessage;
    
    private String localizedErrorTitle;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        saveClicked = false;
    }    
    
    public void setMainApp(ScheduleManager mainApp) {
        this.mainApp = mainApp;
        
        apptCustomerIDColumn.setCellValueFactory(cellData -> cellData.getValue().getCustomerIDProperty().asObject());
        apptCustomerNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCustomerNameProperty());
        apptCustomerTable.setItems(mainApp.getCustomerManager().getCustomers());
        
        startTimeList.setItems(ClockLister.getClock());
        endTimeList.setItems(ClockLister.getClock());
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    @FXML
    public void handleCancel() {
        saveClicked = false;
        dialogStage.close();
    }
    
    @FXML
    public void handleSave() throws SQLException {
        if(validInput()) {
            int apptID = mainApp.getAppointmentManager().autoGenID();
            int customerID = apptCustomerTable.getSelectionModel().getSelectedItem().getCustomerID();
            String title = titleField.getText();
            String description = descriptionTextArea.getText();
            String contact = contactField.getText();
            String location = locationField.getText();
            String url = urlField.getText();
            
            LocalDate apptDate = apptDatePicker.getValue();
            LocalTime startTime = startTimeList.getSelectionModel().getSelectedItem();
            LocalTime endTime = endTimeList.getSelectionModel().getSelectedItem();
            LocalDateTime start = LocalDateTime.of(apptDate, startTime);
            LocalDateTime end = LocalDateTime.of(apptDate, endTime);
            
            Timestamp startStamp = Timestamp.valueOf(start);
            Timestamp endStamp = Timestamp.valueOf(end);
            
            appointment = new Appointment(apptID, customerID, title, description,
                                         location, contact, url, startStamp, 
                                         endStamp, mainApp.getLoggedInUser().getUserName());
            
            mainApp.getAppointmentManager().addAppointment(mainApp.getConnection(), appointment);
            saveClicked = true;
            dialogStage.close();
        }
        else {
            getLocalizedInvalidAppointmentError();
            showWarningAlert(localizedErrorMessage);
        }
    }
    
    private boolean validInput() {
        boolean valid = true;
        
        if(startTimeList.getSelectionModel().getSelectedItem() != null &&
                endTimeList.getSelectionModel().getSelectedItem() != null) {
            
            LocalTime startTime = startTimeList.getSelectionModel().getSelectedItem();
            LocalTime endTime = endTimeList.getSelectionModel().getSelectedItem();

            if(startTime.isAfter(endTime)) {
                valid = false;
            }

            //Business hours 8am - 6pm local time
            int opening = 8; //8am
            int closing = 20;//6pm
            if(startTime.isBefore(LocalTime.of(opening, 0)) || 
                    startTime.isAfter(LocalTime.of(closing, 0))) {
                valid = false;
            }
            
            if(endTime.isBefore(LocalTime.of(opening, 0)) || 
                    endTime.isAfter(LocalTime.of(closing, 0))) {
                valid = false;
            }

            if(apptCustomerTable.getSelectionModel().getSelectedItem() == null) {
                valid = false;
            }

            if(overlappingAppointment()) {
                valid = false;
            }
            
            if(apptDatePicker.getValue() == null) {
                valid = false;
            }
            
            if(titleField.getText().equals("")) {
                valid = false;
            }
            
            if(locationField.getText().equals("")) {
                valid = false;
            }
            
            if(contactField.getText().equals("")) {
                valid = false;
            }
            
            if(urlField.getText().equals("")) {
                valid = false;
            }
            
            if(descriptionTextArea.getText().length() > 255 || 
                    descriptionTextArea.getText().equals("")) {
                valid = false;
            }
        }
        else {
            valid = false;
        }
           
        return valid;
    }
    
    public boolean isSaveClicked() {
        return this.saveClicked;
    }
    
    public void showWarningAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(localizedErrorTitle);
        alert.setHeaderText(message);

        alert.showAndWait(); 
    }
    
    public void getLocalizedInvalidAppointmentError() {
        ResourceBundle rb;
        
        if(mainApp.getUserLocale() == Locale.US) {
            rb = ResourceBundle.getBundle("config.language_en");
            localizedErrorMessage = rb.getString("InvalidAppointmentError");
            localizedErrorTitle = rb.getString("InvalidAppointmentTitle");
        } 
        else {
            rb = ResourceBundle.getBundle("config.language_fr");
            localizedErrorMessage = rb.getString("InvalidAppointmentError");
            localizedErrorTitle = rb.getString("InvalidAppointmentTitle");
        }
    }
    
    public boolean overlappingAppointment() {
        boolean overlapping = false;
        
        if(startTimeList.getSelectionModel().getSelectedItem() != null && 
                endTimeList.getSelectionModel().getSelectedItem() != null) {
            
            LocalTime startTime = startTimeList.getSelectionModel().getSelectedItem();
            LocalTime endTime = endTimeList.getSelectionModel().getSelectedItem();
            LocalDate date = apptDatePicker.getValue();
            
            for(Appointment appt : mainApp.getAppointmentManager().getAppointments()) {
                
                LocalDateTime apptDateTime = appt.getStart().toLocalDateTime();
                LocalDate apptDate = apptDateTime.toLocalDate();
                LocalTime apptStartTime = apptDateTime.toLocalTime();
                LocalTime apptEndTime = appt.getEnd().toLocalDateTime().toLocalTime();
                
                if(daysAreEqual(date, apptDate) && 
                        determineOverlap(startTime, endTime, apptStartTime, apptEndTime)) {
                    overlapping = true;
                }
            }
        }
        else {
            overlapping = true;
        }
        
        return overlapping;
    }
    
    public boolean daysAreEqual(LocalDate date1, LocalDate date2) {
        boolean daysEqual = true;
        
        if(date1.getYear() != date2.getYear()) {
            daysEqual = false;
        }
        if(date1.getDayOfYear() != date2.getDayOfYear()) {
            daysEqual = false;
        }
        
        return daysEqual;
    }
    
    public boolean determineOverlap(LocalTime newApptStartTime, LocalTime newApptEndTime,
                                    LocalTime start, LocalTime end) {
        boolean overlap1 = false;
        boolean overlap2 = false;
        
        if(newApptStartTime.isAfter(start) && newApptStartTime.isBefore(end)) {
            overlap1 = true;
        }
        
        if(newApptStartTime.compareTo(start) == 0 || 
                newApptStartTime.compareTo(end) == 0) {
            overlap1 = true;
        }
        
        if(newApptEndTime.isAfter(start) && newApptEndTime.isBefore(end)) {
            overlap2 = true;
        }
        
        if(newApptEndTime.compareTo(start) == 0 || 
                newApptEndTime.compareTo(end) == 0) {
            overlap2 = true;
        }
        
        return (overlap1 || overlap2);
    }
}
