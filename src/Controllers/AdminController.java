/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author benza
 */
public class AdminController {
    private Connection con;
    public AdminController(Connection con){
        this.con=con;
    }
    
    public ResultSet getCashiers(int prefix) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM cashiers offset ? rows fetch first 4 rows only");
        stmt.setInt(1,prefix*4);
        return stmt.executeQuery();
    }
    
    public void registerCashier(String firstname, String lastname, String email, String phonenumber, String sex, String picture, String password, Date st, Date ed, int salary) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("INSERT INTO cashiers(firstname, lastname, email, password, phonenumber, sex, pictureurl, creationdate) VALUES (?,?,?,?,?,?,?,?)");
        stmt.setString(1,firstname);
        stmt.setString(2,lastname);
        stmt.setString(3, email);
        stmt.setString(4,password);
        stmt.setString(5,phonenumber);
        stmt.setString(6,sex);
        stmt.setString(7, picture);
        stmt.setDate(8, new java.sql.Date(new java.util.Date().getTime()));
        stmt.executeUpdate();
        registerContract(st,ed,salary,new Controllers.ConnectionController(this.con).getCashierId(email));
    }
    
    private void registerContract(Date st, Date ed, int salary, int ci) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("INSERT INTO contracts(startdate, enddate, salary, cashierid) VALUES (?,?,?,?)");
        stmt.setDate(1,st);
        stmt.setDate(2,ed);
        stmt.setInt(3,salary);
        stmt.setInt(4,ci);
        stmt.executeUpdate();
    }
    
    
    public ResultSet getProducts(int prefix) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM products offset ? rows fetch first 4 rows only");
        stmt.setInt(1,prefix*4);
        return stmt.executeQuery();
    }
    
    
    public void addProduct(String category, String subcategory, String name, int quantity, double price, String picture) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("INSERT INTO products(category, subcategory, name, quantity, price, pictureurl, creationdate) VALUES (?,?,?,?,?,?,?)");
        stmt.setString(1,category);
        stmt.setString(2,subcategory);
        stmt.setString(3,name);
        stmt.setInt(4,quantity);
        stmt.setDouble(5, price);
        stmt.setString(6,picture);
        stmt.setDate(7, new java.sql.Date(new java.util.Date().getTime()));
        stmt.executeUpdate();
    }
    
    public Object[] getProductCategories(){
        ArrayList<String> fetchedproducts = new ArrayList<String>();
        PreparedStatement stmt;
        fetchedproducts.add("New");
        try {
            stmt = this.con.prepareStatement("SELECT category FROM products GROUP BY category");
             ResultSet res= stmt.executeQuery();
             while(res.next()){
                 fetchedproducts.add(res.getString("category"));
             }
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fetchedproducts.toArray();
    }
    
    public Object[] getProductSubCategories(String cat){
        ArrayList<String> fetchedproducts = new ArrayList<String>();
        PreparedStatement stmt;
        try {
            stmt = this.con.prepareStatement("SELECT subcategory FROM products WHERE category=? GROUP BY subcategory");
            stmt.setString(1,cat);
             ResultSet res= stmt.executeQuery();
             while(res.next()){
                 fetchedproducts.add(res.getString("subcategory"));
             }
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        fetchedproducts.add("New");
        return fetchedproducts.toArray();
    }
    
    
    public ResultSet getCustomers(int prefix) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM customers offset ? rows fetch first 4 rows only");
        stmt.setInt(1,prefix*4);
        return stmt.executeQuery();
    }
    
    
    public void registerCustomer(String firstname, String lastname, String email, String phonenumber, String sex, String picture, String address) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("INSERT INTO customers(firstname, lastname, email, address, phonenumber, sex, pictureurl, creationdate) VALUES (?,?,?,?,?,?,?,?)");
        stmt.setString(1,firstname);
        stmt.setString(2,lastname);
        stmt.setString(3, email);
        stmt.setString(4,address);
        stmt.setString(5,phonenumber);
        stmt.setString(6,sex);
        stmt.setString(7, picture);
        stmt.setDate(8, new java.sql.Date(new java.util.Date().getTime()));
        stmt.executeUpdate();
    }
}
