/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;
import Views.AdminDashboardView;

import Models.AdminModel;
import Models.CashierModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author benza
 */
public class ConnectionController {
    private Connection con;
    public ConnectionController(Connection con){
        this.con= con;
    }
    
    public boolean isAdmin(int id, String password){
        try{
            PreparedStatement stmt = this.con.prepareStatement("SELECT id FROM admins WHERE id=? AND PASSWORD=?");
            stmt.setInt(1, id);
            stmt.setString(2, password);
            return stmt.executeQuery().next();
        }catch(SQLException ex){
            return false;
        }
    }
    
    public AdminModel getAdmin(int id, String password) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM admins WHERE id=? AND PASSWORD=?");
        stmt.setInt(1, id);
        stmt.setString(2, password);
        return new AdminModel(stmt.executeQuery());
    }
    
    public boolean isCashier(int id, String password){
        try{
            PreparedStatement stmt = this.con.prepareStatement("SELECT id FROM cashiers WHERE id=? AND PASSWORD=?");
            stmt.setInt(1, id);
            stmt.setString(2, password);
            return stmt.executeQuery().next();
        }catch(SQLException ex){
            return false;
        }
    }
    
    public int getCashierId(String email) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT id FROM cashiers WHERE email=?");
        stmt.setString(1, email);
        ResultSet res= stmt.executeQuery();
        res.next();
        return res.getInt("id");
    }
    
    public CashierModel getCashier(int id, String password) throws SQLException{
        PreparedStatement stmt = this.con.prepareStatement("SELECT * FROM cashiers WHERE id=? AND PASSWORD=?");
        stmt.setInt(1, id);
        stmt.setString(2, password);
        return new CashierModel(this.con,stmt.executeQuery());
    }
}
