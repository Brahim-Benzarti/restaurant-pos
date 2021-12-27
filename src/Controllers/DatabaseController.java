/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author benza
 */
public class DatabaseController{
    private Connection con;
    public DatabaseController() throws SQLException{
        this.con= DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectJava","root","123");
    }
    
    public Connection con(){
        return this.con;
    }
    
    public void migrateFresh() throws SQLException{
        Statement stmt = this.con.createStatement();
        stmt.execute("DROP TABLE users;");
        System.out.println("Table users dropped!");
        stmt.execute("CREATE TABLE users( id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), firstname VARCHAR(100) NOT NULL, lastname VARCHAR(100) NOT NULL, email VARCHAR(200) NOT NULL, phonenumber VARCHAR(8) NOT NULL, sex char CHECK (sex='M' OR sex='F') NOT NULL, pictureurl VARCHAR(300) NOT NULL, creationdate DATE );");
        System.out.println("Table users created successfully!");
    }
}
