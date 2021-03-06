/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.CashierModel;
import Models.CustomerModel;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import org.jfree.data.general.DefaultPieDataset;

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
    
    public void updateCashier(int id, String firstname, String lastname, String email, String phonenumber, String sex, String picture, Date ed, int salary) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("UPDATE cashiers SET firstname=?, lastname=?, email=?, phonenumber=?, sex=?, pictureurl=? WHERE id=?");
        stmt.setString(1,firstname);
        stmt.setString(2,lastname);
        stmt.setString(3, email);
        stmt.setString(4,phonenumber);
        stmt.setString(5,sex);
        stmt.setString(6, picture);
        stmt.setInt(7, id);
        stmt.executeUpdate();
        updateContract(ed,salary,id);
    }
    
    private void updateContract(Date ed, int salary, int ci) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("UPDATE contracts SET enddate=?, salary=? WHERE cashierid=?");
        stmt.setDate(1,ed);
        stmt.setInt(2,salary);
        stmt.setInt(3,ci);
        stmt.executeUpdate();
    }
    
    public void removeCashier(int cashierid) throws SQLException{
        PreparedStatement stmt0 = this.con.prepareStatement("DELETE FROM contracts WHERE cashierid=?");
        stmt0.setInt(1,cashierid);
        stmt0.executeUpdate();
        PreparedStatement stmt = this.con.prepareStatement("DELETE FROM cashiers WHERE id=?");
        stmt.setInt(1,cashierid);
        stmt.executeUpdate();
    }
    
    public void removeCustomer(int customerid) throws SQLException{
        PreparedStatement stmt0 = this.con.prepareStatement("UPDATE carts SET customerid=0 WHERE customerid=?");
        stmt0.setInt(1,customerid);
        stmt0.executeUpdate();
        PreparedStatement stmt = this.con.prepareStatement("DELETE FROM customers WHERE id=?");
        stmt.setInt(1,customerid);
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
    
    public Object[] getProductCategories(boolean cashier){
        ArrayList<String> fetchedproducts = new ArrayList<String>();
        PreparedStatement stmt;
        if(!cashier){
            fetchedproducts.add("New");
        }
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
    
    public Object[] getProductSubCategories(String cat, boolean cashier){
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
        if(!cashier){
            fetchedproducts.add("New");
        }
        return fetchedproducts.toArray();
    }
    
    
    public ResultSet getCustomers(int prefix) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM customers offset ? rows fetch first 4 rows only");
        //the one is just because of the unregistered customer
        stmt.setInt(1,1+prefix*4);
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
    
    public void updateCustomer(int id, String firstname, String lastname, String email, String phonenumber, String sex, String picture, String address) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("UPDATE customers SET firstname=?, lastname=?, email=?, address=?, phonenumber=?, sex=?, pictureurl=? WHERE id=?");
        stmt.setString(1,firstname);
        stmt.setString(2,lastname);
        stmt.setString(3, email);
        stmt.setString(4,address);
        stmt.setString(5,phonenumber);
        stmt.setString(6,sex);
        stmt.setString(7, picture);
        stmt.setInt(8, id);
        stmt.executeUpdate();
    }
    
    public DefaultPieDataset defaultPieChart() throws SQLException{
        DefaultPieDataset respie = new DefaultPieDataset();
        PreparedStatement stmt = this.con.prepareStatement(
            "SELECT products.category, COUNT(orders.quantity) AS sold "+
            "FROM products, orders, carts " +
            "WHERE products.id=orders.productid " +
            "AND orders.cartid=carts.id " +
            "AND UPPER(carts.status)='PAID' " +
            "GROUP BY products.category"
        );
        ResultSet res= stmt.executeQuery();
        while(res.next()){
            respie.setValue(res.getString("category"),res.getInt("sold"));
        }
        return respie;
    }
    
    public int getTopCustomer() throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT customerid FROM carts WHERE UPPER(status)='PAID' AND customerid<>0 GROUP BY customerid ORDER BY SUM(total) DESC FETCH FIRST ROW ONLY");
        ResultSet res= stmt.executeQuery();
        res.next();
        return res.getInt("customerid");
    }
    
    public CustomerModel getCustomer(int id) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM customers WHERE id=?");
        stmt.setInt(1, id);
        return new CustomerModel(stmt.executeQuery());
    }
    
    public CategoryDataset defaultLineChart() throws SQLException{
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        PreparedStatement stmt = this.con.prepareStatement("SELECT creationdate, SUM(total) AS ttotal FROM carts GROUP BY creationdate ORDER BY creationdate fetch first 30 rows only");
        ResultSet res= stmt.executeQuery();
        while(res.next()){
            dataset.addValue(res.getDouble("ttotal"),"",res.getString("creationdate"));
        }
        return dataset;
    }
    
    public CashierModel getCashierByIndex(int prefix) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM cashiers offset ? rows fetch first row only");
        stmt.setInt(1,prefix);
        return new CashierModel(this.con, stmt.executeQuery());
    }
    
    public CustomerModel getCustomerByIndex(int prefix) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM customers WHERE id<>0 offset ? rows fetch first row only");
        stmt.setInt(1,prefix);
        return new CustomerModel(stmt.executeQuery());
    }
    
    public Object[] getCashierContruct(int cashierid) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM contracts WHERE id=?");
        stmt.setInt(1,cashierid);
        ResultSet res = stmt.executeQuery();
        res.next();
        return new Object[] {res.getDate("enddate"),res.getInt("salary"), res.getDate("startdate")};
    }
    
    public void publishMessage(String message, String author) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("INSERT INTO adminmessages(message,author,creationdate) VALUES (?,?,?)");
        stmt.setString(1,message);
        stmt.setString(2,author);
        stmt.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
        stmt.executeUpdate();
    }
}
