/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.ProductModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author benza
 */
public class CashierController {
    private Connection con;
    public CashierController(Connection con){
        this.con = con;
    }
    
    
    public Object[] getProducts(String cat, String subcat){
        ArrayList<String> fetchedproducts = new ArrayList<String>();
        PreparedStatement stmt;
        try {
            stmt = this.con.prepareStatement("SELECT name, quantity, price, pictureurl FROM products WHERE category=? AND subcategory=?");
            stmt.setString(1,cat);
            stmt.setString(2,subcat);
             ResultSet res= stmt.executeQuery();
             while(res.next()){
                 fetchedproducts.add(res.getString("name"));
             }
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fetchedproducts.toArray();
    }
    
    public int getTotalPendingOrders() throws SQLException{
        PreparedStatement stmt= this.con.prepareStatement("SELECT COUNT(*) AS total FROM carts WHERE UPPER(status)='FENDING'");
        ResultSet res= stmt.executeQuery();
        res.next();
        return res.getInt("total");
    }
    
    public ResultSet getOrders(int prefix) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM carts WHERE UPPER(status)='PENDING' ORDER BY creationdate offset ? rows fetch first 4 rows only");
        stmt.setInt(1,prefix*4);
        return stmt.executeQuery();
    }
    
    public ProductModel getProduct(String pn) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM products WHERE name=?");
        stmt.setString(1, pn);
        return new ProductModel(stmt.executeQuery());
    }
    
    public Object[] getCustomersList(String like) throws SQLException{
        ArrayList<KeyValue> elm = new ArrayList<KeyValue>();
        like="%"+like.toUpperCase()+"%";
        String phone = like+"%";
        PreparedStatement stmt = this.con.prepareStatement("SELECT id,firstname,lastname FROM customers WHERE UPPER(firstname) LIKE ? OR phonenumber LIKE ?");
        stmt.setString(1, like);
        stmt.setString(2, phone);
        ResultSet res= stmt.executeQuery();
        while(res.next()){
            elm.add(new KeyValue(res.getInt("id"),res.getString("firstname")));
        }
        return elm.toArray();
    }
    
    public String getCustomerStars(int id) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT COUNT(*) AS stars FROM carts WHERE customerid=? AND UPPER(status)='PAID'");
        stmt.setInt(1, id);
        ResultSet res = stmt.executeQuery();
        res.next();
        return String.valueOf(res.getInt("stars"));
    }
}
