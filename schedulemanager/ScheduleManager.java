/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import schedulemanager.model.Address;
import schedulemanager.model.AddressManager;
import schedulemanager.model.Appointment;
import schedulemanager.model.AppointmentManager;
import schedulemanager.model.CityManager;
import schedulemanager.model.CountryManager;
import schedulemanager.model.Customer;
import schedulemanager.model.CustomerManager;
import schedulemanager.model.DatabaseManager;
import schedulemanager.model.User;
import schedulemanager.model.UserManager;
import schedulemanager.view_controller.AddAppointmentController;
import schedulemanager.view_controller.AddCustomerController;
import schedulemanager.view_controller.LoginFormController;
import schedulemanager.view_controller.MainMenuController;
import schedulemanager.view_controller.ModifyAppointmentController;
import schedulemanager.view_controller.ModifyCustomerController;
import schedulemanager.view_controller.MonthCalendarController;
import schedulemanager.view_controller.WeeklyCalendarController;
import test.DatabaseSeeder;
import test.TestDatabaseInteractions;

/**
 *
 * @author scott
 */
public class ScheduleManager extends Application {
    private CustomerManager customerManager;
    private AddressManager addressManager;
    private UserManager userManager;
    private AppointmentManager appointmentManager;
    private CityManager cityManager;
    private CountryManager countryManager;
    private Stage primaryStage;
    private User loggedInUser;
    private boolean userLoggedIn;
    private Connection dbConnection;
    private Locale userLocale;
    
    @Override
    public void start(Stage stage) throws Exception {
        DatabaseManager dbManager = new DatabaseManager(
                        DatabaseRouter.testDBName, DatabaseRouter.testDBURL,
                        DatabaseRouter.testDBUsername, DatabaseRouter.testDBPassword);
        
        dbConnection = dbManager.getConnection();
        customerManager = new CustomerManager();
        customerManager.loadCustomers(dbConnection);
        addressManager = new AddressManager();
        addressManager.loadAddresses(dbConnection);
        userManager = new UserManager();
        userManager.loadUsers(dbConnection);
        appointmentManager = new AppointmentManager();
        appointmentManager.loadAppointments(dbConnection);
        cityManager = new CityManager();
        cityManager.loadCities(dbConnection);
        countryManager = new CountryManager();
        countryManager.loadCountries(dbConnection);
        this.primaryStage = stage;
        userLoggedIn = false;
        userLocale = Locale.getDefault();
        
        showLogin();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public Address getCustomerAddress(Customer customer) {
        return addressManager.getAddress(customer.getAddressID());
    }
    
    public CustomerManager getCustomerManager() {
        return this.customerManager;
    }
    
    public AddressManager getAddressManager() {
        return this.addressManager;
    }
    
    public UserManager getUserManager() {
        return this.userManager;
    }
    
    public AppointmentManager getAppointmentManager() {
        return this.appointmentManager;
    }
    
    public CityManager getCityManager() {
        return this.cityManager;
    }
    
    public CountryManager getCountryManager() {
        return this.countryManager;
    }
    
    public void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ScheduleManager.class.getResource(ViewRouter.loginURL));
            Parent login = loader.load();
              
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(login);
            dialogStage.setScene(scene);
            LoginFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);
            controller.setUserLocation();
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            
            if(controller.getSuccessfulLogin()) {
                setLoggedInUser(controller.getUser());
                setUserLoggedIn(controller.getSuccessfulLogin());
                showMainMenu();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ScheduleManager.class.getResource(ViewRouter.mainMenuURL));
            Parent mainMenu = loader.load();
              
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(mainMenu);
            dialogStage.setScene(scene);
            MainMenuController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean showAddCustomer() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ScheduleManager.class.getResource(ViewRouter.addCustomerURL));
        Parent addCustomer = loader.load();


        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(addCustomer);
        dialogStage.setScene(scene);
        AddCustomerController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMainApp(this);
        // Show dialog stage and wait until it's closed
        dialogStage.showAndWait();

        return controller.isSaveClicked(); 
    }
    
    public boolean showModifyCustomer(Customer customer) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ScheduleManager.class.getResource(ViewRouter.modifyCustomerURL));
        Parent modCustomer = loader.load();
         
        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(modCustomer);
        dialogStage.setScene(scene);
        ModifyCustomerController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMainApp(this);
        controller.setCustomer(customer);
        //Show dialog stage and wait until it's closed
        dialogStage.showAndWait();
        
        return controller.isSaveClicked();
    }
    
    public boolean showAddAppointment() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ScheduleManager.class.getResource(ViewRouter.addAppointmentURL));
        Parent addAppointment = loader.load();
         
        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(addAppointment);
        dialogStage.setScene(scene);
        AddAppointmentController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMainApp(this);
        //Show dialog stage and wait until it's closed
        dialogStage.showAndWait();
        
        return controller.isSaveClicked();
    }
    
    public boolean showModifyAppointment(Appointment appointment) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ScheduleManager.class.getResource(ViewRouter.modifyAppointmentURL));
        Parent modAppointment = loader.load();
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(modAppointment);
        dialogStage.setScene(scene);
        ModifyAppointmentController controller = loader.getController();
        
        controller.setDialogStage(dialogStage);
        controller.setMainApp(this);
        controller.setAppointment(appointment);
        
        dialogStage.showAndWait();
        
        return controller.isSaveClicked();
    }
    
    public void showWeeklyCalendar(LocalDateTime dateTime) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ScheduleManager.class.getResource(ViewRouter.weeklyCalendarURL));
        Parent weeklyCalendar = loader.load();
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(weeklyCalendar);
        dialogStage.setScene(scene);
        WeeklyCalendarController controller = loader.getController();
        
        controller.setDialogStage(dialogStage);
        controller.setMainApp(this);
        controller.setDate(dateTime);
        
        dialogStage.showAndWait();        
    }
    
    public void showMonthCalendar(LocalDateTime dateTime) throws IOException {
   FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ScheduleManager.class.getResource(ViewRouter.monthCalendarURL));
        Parent monthCalendar = loader.load();
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(monthCalendar);
        dialogStage.setScene(scene);
        MonthCalendarController controller = loader.getController();
        
        controller.setDialogStage(dialogStage);
        controller.setMainApp(this);
        controller.setDate(dateTime);
        
        dialogStage.showAndWait();        
    }
    
    public void setUserLoggedIn(boolean bool) {
        this.userLoggedIn = bool;
    }
    
    public User getLoggedInUser() {
        return this.loggedInUser;
    }
    
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }
    
    public Connection getConnection() {
        return this.dbConnection;
    }
    
    public void seedDatabase() {
        DatabaseSeeder dbSeeder = new DatabaseSeeder(dbConnection, countryManager,
                                      cityManager, addressManager, customerManager,
                                      appointmentManager, userManager);
        
        dbSeeder.seedUserTable();
    }
    
    public void testManagers() {
        TestDatabaseInteractions dbTester = new TestDatabaseInteractions(dbConnection,
                                            userManager, appointmentManager,
                                            customerManager, addressManager,
                                            cityManager, countryManager);
        
        dbTester.testUserCRUD();
        dbTester.testCustomerCRUD();
        dbTester.testAddressCRUD();
        dbTester.testCityCRUD();
        dbTester.testCountryCRUD();
        dbTester.testAppointmentCRUD();
    }
    
    public Locale getUserLocale() {
        return this.userLocale; //Use this to see your default locale
//        return Locale.FRANCE; //Use this to see the translated locale
    }
}
