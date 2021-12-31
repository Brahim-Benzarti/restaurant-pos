/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author benza
 */
public class ProductModel {
    public int id;
    public String category;
    public String subcategory;
    public String name;
    public String pictureurl;
    public int quantity;
    public double price;
    public Date creationdate;
    public ProductModel(ResultSet res) throws SQLException{
        res.next();
        this.id=res.getInt("id");
        this.category=res.getString("category");
        this.subcategory=res.getString("subcategory");
        this.name=res.getString("name");
        this.pictureurl=res.getString("pictureurl");
        this.quantity=res.getInt("quantity");
        this.price=res.getDouble("price");
        this.creationdate=res.getDate("creationdate");
    }
    
    public boolean isAvailable(){
        return this.quantity>0;
    }
    
    public boolean isAvailable(int n){
        return this.quantity-n>=0;
    }
}
