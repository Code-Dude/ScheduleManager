/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.view_controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import schedulemanager.ScheduleManager;
import schedulemanager.model.Appointment;
import schedulemanager.model.Customer;
import schedulemanager.model.User;
import schedulemanager.model.WeeklyView;
import schedulemanager.model.YearManager;

/**
 * FXML Controller class
 *
 * @author scott
 */
public class MainMenuController implements Initializable {
    
    @FXML
    private TableColumn<Customer, Integer> customerIDColumn;
    
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    
    @FXML
    private TableColumn<Customer, String> customerAddressColumn;

    @FXML
    private TableView<Customer> customerTable;
    
    @FXML
    private TableColumn<Appointment, String> apptTimeColumn;
    
    @FXML
    private TableColumn<Appointment, String> apptCustomerColumn;
    
    @FXML
    private TableView<Appointment> apptTable;

    @FXML
    private Button addCustomerBtn;
    
    @FXML
    private Button modifyCustomerBtn;

    @FXML
    private Button deleteCustomerBtn;
    
    @FXML
    private Button addApptBtn;
    
    @FXML
    private Button modifyApptBtn;
    
    @FXML
    private Button deleteApptBtn;
    
    @FXML
    private Button exitBtn;
    
    @FXML
    private Button weeklyBtn;
    
    @FXML
    private Button monthBtn;
    
    @FXML
    private Button appointmentReportBtn;
    
    @FXML
    private Button scheduleBtn;
    
    @FXML
    private Button customerApptBtn;
    
    private ScheduleManager mainApp;
    
    private Stage dialogStage;
    
    private String appointmentSoonMessage;
    
    private String appointmentSoonTitle;
        
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerIDColumn.setCellValueFactory(cellData -> cellData.getValue().getCustomerIDProperty().asObject());
        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCustomerNameProperty());
        customerAddressColumn.setCellValueFactory(cellData -> mainApp.getAddressManager().getAddress(cellData.getValue().getAddressID()).getAddressProperty());
        
        apptTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getStartWithTimeZoneStringProperty());
        apptCustomerColumn.setCellValueFactory(cellData -> mainApp.getCustomerManager().getCustomer(cellData.getValue().getCustomerID()).getCustomerNameProperty());
    }    
    
    public void setMainApp(ScheduleManager mainApp) {
        this.mainApp = mainApp;
        
        customerTable.setItems(mainApp.getCustomerManager().getCustomers());
        apptTable.setItems(mainApp.getAppointmentManager().getAppointments());
        if(mainApp.getAppointmentManager().appointmentSoon(mainApp.getLoggedInUser())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            getLocalizedAppointmentMessages();
            alert.setTitle(appointmentSoonTitle);
            alert.setHeaderText(appointmentSoonMessage);

            alert.showAndWait(); 
        }
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void refreshCustomerTableView() {
        customerTable.setItems(mainApp.getCustomerManager().getCustomers());
        customerTable.refresh();
    }
    
    public void refreshApptTable() {
        apptTable.setItems(mainApp.getAppointmentManager().getAppointments());
        apptTable.refresh();
    }
    
    @FXML
    public void handleAddCustomer() throws SQLException {
        try {
            if(mainApp.showAddCustomer()) {
                refreshCustomerTableView();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleDeleteCustomer() throws SQLException {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        if(customer != null) {
            ObservableList<Appointment> appointments = FXCollections.observableArrayList(mainApp.getAppointmentManager().getAppointments());
            for(Appointment appointment : appointments) {
                if(appointment.getCustomerID() == customer.getCustomerID()) {
                    mainApp.getAppointmentManager().deleteAppointment(mainApp.getConnection(), appointment);
                }
            }
            mainApp.getCustomerManager().deleteCustomer(mainApp.getConnection(), customer);
            refreshCustomerTableView();
            refreshApptTable();
        }
    }
    
    @FXML
    public void handleModifyCustomer() throws SQLException {
        try {
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            if(customer != null) {
                boolean saveClicked = mainApp.showModifyCustomer(customer);
                if(saveClicked) {
                    mainApp.getCustomerManager().updateCustomer(mainApp.getConnection(), 
                                                 customer, mainApp.getLoggedInUser().getUserName());
                    refreshCustomerTableView();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML 
    void handleAddAppointment() throws SQLException {
        boolean saveClicked = false;
        
       try {
            saveClicked = mainApp.showAddAppointment();
            if(saveClicked) {
                refreshApptTable();
            }
       } catch (IOException e) {
           e.printStackTrace();
       }
    }
    
    @FXML
    void handleModifyAppointment() throws SQLException {
        try {
            Appointment appointment = apptTable.getSelectionModel().getSelectedItem();
            if(appointment != null) {
                if(mainApp.showModifyAppointment(appointment)) {
                    refreshApptTable(); 
                }
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleDeleteAppointment() throws SQLException {
        Appointment appointment = apptTable.getSelectionModel().getSelectedItem();
        if(appointment != null) {
            mainApp.getAppointmentManager().deleteAppointment(mainApp.getConnection(), appointment);
            refreshApptTable();
        }
    }
    
    @FXML
    public void handleExit() {
        System.exit(0);
    }
    
    @FXML
    private void handleShowWeeklyCalendar() throws IOException {
        LocalDateTime today = LocalDateTime.now();
        mainApp.showWeeklyCalendar(today);
    }
    
    @FXML
    private void handleShowMonthCalendar() throws IOException {
        LocalDateTime today = LocalDateTime.now();
        mainApp.showMonthCalendar(today);
    }
    
    public void getLocalizedAppointmentMessages() {
        ResourceBundle rb;
        
        if(mainApp.getUserLocale() == Locale.US) {
            rb = ResourceBundle.getBundle("config.language_en");
            appointmentSoonMessage = rb.getString("AppointmentSoonMessage");
            appointmentSoonTitle = rb.getString("AppointmentSoonTitle");
        } 
        else {
            rb = ResourceBundle.getBundle("config.language_fr");
            appointmentSoonMessage = rb.getString("AppointmentSoonMessage");
            appointmentSoonTitle = rb.getString("AppointmentSoonTitle");
        }
    }
    
    @FXML
    public void handleAppointmentByMonthReport() {
        try {
            ObservableList<Appointment> appts = mainApp.getAppointmentManager().getAppointments();
            YearManager yearManager = new YearManager(mainApp.getAppointmentManager());
            ArrayList<WeeklyView> year = yearManager.getYear(LocalDateTime.now());
            
            File dir = new File("projReports");
            File file = new File("projReports/appointments.txt");
            
            if(!dir.exists()) {
                dir.mkdirs();  
            }
            
            if(!file.exists()) {
                file.createNewFile();
            }
            
            PrintWriter pw = new PrintWriter(file);
            
            for(int i = 1; i <= 12; i++) {
                
                Month month = Month.of(i);
                
                pw.println(month);
                
                for(WeeklyView day : year) {
                    if(day.getDateTime().getMonth() == month) {
                        if(day.getNumAppts() > 0) {
                            pw.println(day);
                        }
                    }
                }
            }
            
            showReportAlert();
            
            pw.close();
        } catch(IOException e) {
            System.out.println("Issue creating report.");
        }
    }
    
    @FXML
    public void handleScheduleOfUsersReport() {
        try {
            File dir = new File("projReports");
            File file = new File("projReports/schedule.txt");
            
            if(!dir.exists()) {
                dir.mkdirs();  
            }
            
            if(!file.exists()) {
                file.createNewFile();
            }
            
            PrintWriter pw = new PrintWriter(file);
            
            for(User user : mainApp.getUserManager().getUsers()) {
                
                pw.println(user.getUserName());
                pw.println("---------------------");
                
               for(Appointment appt : mainApp.getAppointmentManager().getAppointments()) {
                   if(appt.getLastUpdateBy().equals(user.getUserName())) {
                       pw.println(appt);
                   }
               }
            }
            
            showReportAlert();
            pw.close();
            
        } catch(IOException e) {
            System.out.println("Issue creating report.");
        }
    }
    
    @FXML
    public void handleCustomerAppointmentReport() {
        try {
            File dir = new File("projReports");
            File file = new File("projReports/customerAppointments.txt");
            
            if(!dir.exists()) {
                dir.mkdirs();  
            }
            
            if(!file.exists()) {
                file.createNewFile();
            }
            
            PrintWriter pw = new PrintWriter(file);
            
            for(Customer customer : mainApp.getCustomerManager().getCustomers()) {
                
                pw.println(customer.getCustomerID() + " " + customer.getCustomerName());
                pw.println("--------------------------------");
                
               for(Appointment appt : mainApp.getAppointmentManager().getAppointments()) {
                   if(appt.getCustomerID() == customer.getCustomerID()) {
                       pw.println(appt);
                   }
               }
            }
            
            showReportAlert();
            pw.close();
            
        } catch(IOException e) {
            System.out.println("Issue creating report.");
        }
    }
    
    public void showReportAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Generated Report");
        alert.setHeaderText("Created report, you can find it in the "
                + "projReports directory.");

        alert.showAndWait(); 
    }
}
