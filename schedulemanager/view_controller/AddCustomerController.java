/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.view_controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import schedulemanager.ScheduleManager;
import schedulemanager.model.Address;
import schedulemanager.model.City;
import schedulemanager.model.Country;
import schedulemanager.model.Customer;

/**
 * FXML Controller class
 *
 * @author scott
 */
public class AddCustomerController implements Initializable {
    
    @FXML
    private TextField customerNameField;
    
    @FXML
    private TextField activeField;
    
    @FXML
    private TextField addressField;
    
    @FXML
    private TextField address2Field;
    
    @FXML
    private TextField postalCodeField;
    
    @FXML
    private TextField phoneNumberField;
            
    @FXML
    private TextField cityField;
    
    @FXML
    private TextField countryField;
    
    @FXML
    private Button cancelModBtn;
    
    @FXML
    private Button saveModBtn;
    
    private Stage dialogStage;
    
    private ScheduleManager mainApp;
    
    private Customer customer;
    
    private Address address;
    
    private City city;
    
    private Country country;
    
    private boolean saveClicked;
    
    private String invalidCustomerError;
    
    private String invalidCustomerTitle;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        saveClicked = false;
    }    
    
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setMainApp(ScheduleManager mainApp) {
        this.mainApp = mainApp;
    }
    
    public Customer getCustomer() {
        return this.customer;
    }
    
    public Country getCountry() {
        return this.country;
    }
    
    public City getCity() {
        return this.city;
    }
    
    public Address getAddress() {
        return this.address;
    }
    
    @FXML
    public void handleSaveCustomer() throws SQLException {
        try {
            if(validInput()) {
                String userName = mainApp.getLoggedInUser().getUserName();

                int countryID = mainApp.getCountryManager().autoGenID();
                String countryName = countryField.getText();
                country = new Country(countryID, countryName, userName);

                int cityID = mainApp.getCityManager().autoGenID();
                String cityName = cityField.getText();
                city = new City(cityID, cityName, countryID, userName);

                int addressID = mainApp.getAddressManager().autoGenID();
                String address1 = addressField.getText();

                String address2;
                if(address2Field.getText().equals("")) {
                    address2 = "N/A";
                }
                else {
                    address2 = address2Field.getText();
                }

                String postalCode = postalCodeField.getText();
                String phoneNumber = phoneNumberField.getText();
                address = new Address(addressID, address1, address2, cityID,
                              postalCode, phoneNumber, userName);

                int customerID = mainApp.getCustomerManager().autoGenID();
                int active = Integer.parseInt(activeField.getText());
                String name = customerNameField.getText();
                customer = new Customer(customerID, name, addressID, active);

                mainApp.getCountryManager().addCountry(mainApp.getConnection(), country);
                mainApp.getCityManager().addCity(mainApp.getConnection(), city);
                mainApp.getAddressManager().addAddress(mainApp.getConnection(), address);
                mainApp.getCustomerManager().addCustomer(mainApp.getConnection(), customer);

                saveClicked = true;
                dialogStage.close();
            }
            else {
                throw new InvalidCustomerException();
            }
        } catch (InvalidCustomerException e) {
            getLocalizedInvalidCustomerError();
            showWarningAlert(invalidCustomerError);
        }
    }
    
    @FXML
    public void handleCancel() {
        dialogStage.close();
    }
    
    private boolean validInput() {
        boolean valid = true;
        
        if(customerNameField.getText().isEmpty()) {
            valid = false;
        }
        else if(activeField.getText().isEmpty()) {
            valid = false;
        }
        else if(addressField.getText().isEmpty()) {
            valid = false;
        }
        else if(postalCodeField.getText().isEmpty()) {
            valid = false;
        }
        else if(phoneNumberField.getText().isEmpty()) {
            valid = false;
        }
        else if(cityField.getText().isEmpty()) {
            valid = false;
        }
        else if(countryField.getText().isEmpty()) {
            valid = false;
        }
        
        return valid;
    }
    
    public void showWarningAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(invalidCustomerTitle);
        alert.setHeaderText(message);

        alert.showAndWait(); 
    }
    
    public boolean isSaveClicked() {
        return this.saveClicked;
    }
    
    public void getLocalizedInvalidCustomerError() {
        ResourceBundle rb;
        
        if(mainApp.getUserLocale() == Locale.US) {
            rb = ResourceBundle.getBundle("config.language_en");
            invalidCustomerError = rb.getString("InvalidCustomerError");
            invalidCustomerTitle = rb.getString("InvalidCustomerTitle");
        } 
        else {
            rb = ResourceBundle.getBundle("config.language_fr");
            invalidCustomerError = rb.getString("InvalidCustomerError");
            invalidCustomerTitle = rb.getString("InvalidCustomerTitle");
        }
    }
}

class InvalidCustomerException extends Exception {
    public InvalidCustomerException() {
        super();
    }
}
