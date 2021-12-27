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
public class ContractModel {
    private int id;
    public Date startdate;
    public Date enddate;
    public int salary;

    public ContractModel(ResultSet rs) throws SQLException{
        rs.next();
        id=rs.getInt("id");
        startdate=rs.getDate("firstname");
        enddate=rs.getDate("lastname");
        salary=rs.getInt("salary");
    }
}
