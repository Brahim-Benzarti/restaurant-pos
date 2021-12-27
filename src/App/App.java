/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

//Theme
import Views.AdminDashboardView;

//Controllers
import Controllers.DatabaseController;

//Models

//Views
import Views.LoginView;
import Views.ErrorView;


import com.formdev.flatlaf.IntelliJTheme;
import java.sql.SQLException;
import javax.swing.JFrame;

/**
 *
 * @author benza
 */
public class App {
    public static void main(String[] args){
        IntelliJTheme.setup(App.class.getResourceAsStream("/Assets/theme.json" ) );
        try{
            JFrame login = new LoginView(new DatabaseController().con());
        }catch(SQLException ex){
            new ErrorView(ex.getMessage());
        }
    }
}
