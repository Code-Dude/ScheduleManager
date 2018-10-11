/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author scott
 */
public class DatabaseManager {
    private final String dbName;
    private final String dbURL;
    private final String userName;
    private final String password;
    private final Connection dbConnection;
    
    public DatabaseManager(String dbName, String dbURL, String userName,
                           String password) throws SQLException {
        this.dbName = dbName;
        this.dbURL = dbURL;
        this.userName = userName;
        this.password = password;
        
        this.dbConnection = DriverManager.getConnection(this.dbURL,this.userName,
                                                      this.password);
    }
    
    public Connection getConnection() {
        return this.dbConnection;
    }
}
