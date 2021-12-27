/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


/**
 *
 * @author benza
 */
public class CashierModel {
    private int id;
    public String firstname;
    public String lastname;
    public String email;
    public String phonenumber;
    public char sex;
    public String pictureurl;
    private String password;
    public Date creationdate;
    public int totalworkedtime;
    public Models.ContractModel contract;
    
    public CashierModel(Connection con,ResultSet rs) throws SQLException{
        rs.next();
        id=rs.getInt("id");
        firstname=rs.getString("firstname");
        lastname=rs.getString("lastname");
        email=rs.getString("email");
        phonenumber=rs.getString("phonenumber");
        pictureurl=rs.getString("pictureurl");
        password=rs.getString("password");
        sex=rs.getString("sex").charAt(0);
        creationdate=rs.getDate("creationdate");
        totalworkedtime=rs.getInt("totalworkedtime");
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM contracts where id=?");
        stmt.setInt(1,rs.getInt("contractid"));
        contract=new Models.ContractModel(stmt.executeQuery());
    }
    
    public String getFullName(){
        return firstname+" "+lastname;
    }
}
