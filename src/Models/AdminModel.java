/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


/**
 *
 * @author benza
 */
public class AdminModel {
    private int id;
    public String firstname;
    public String lastname;
    public String email;
    public String phonenumber;
    public char sex;
    public String pictureurl;
    private String password;
    public Date creationdate;
    public String position;
    public AdminModel(ResultSet rs) throws SQLException{
        rs.next();
        id=rs.getInt("id");
        firstname=rs.getString("firstname");
        lastname=rs.getString("lastname");
        email=rs.getString("email");
        phonenumber=rs.getString("phonenumber");
        pictureurl=rs.getString("pictureurl");
        password=rs.getString("password");
        position=rs.getString("position");
        sex=rs.getString("sex").charAt(0);
        creationdate=rs.getDate("creationdate");
    }
    
    public String getFullName(){
        return firstname+" "+lastname;
    }
}
