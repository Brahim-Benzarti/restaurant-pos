/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;

/**
 *
 * @author benza
 */
public class GlobalController {
    public static boolean checkInputFields(ArrayList<javax.swing.text.JTextComponent> fields){
        for(javax.swing.text.JTextComponent field: fields){
            if(field.getText().isEmpty()){
                return false;
            }
        }
        return true;
    }
    
    
    public static boolean checkInputFieldsNumeric(ArrayList<javax.swing.text.JTextComponent> fields){
        try{
            for(javax.swing.text.JTextComponent field: fields){
                Integer.parseInt(field.getText());
            }
        }catch(NumberFormatException ex){
            return false;
        }
        return true;
    }
    
    public static boolean checkInputFieldsDate(ArrayList<Date> fields){
        try{
            for(Date field: fields){
                field.toString();
            }
        }catch(NullPointerException ex){
            return false;
        }
        return true;
    }
    
    public static boolean checkinputFieldsPicture(ArrayList<JFileChooser> fields){
        try{
            for(JFileChooser field: fields){
                if(field.getSelectedFile().isFile()){
                    if (field.getSelectedFile().isDirectory()) {
                        return false;
                    }

                    String extension = getExtension(field.getSelectedFile());
                    if (extension != null) {
                        if (!(extension.equals("tiff") ||
                            extension.equals("tif") ||
                            extension.equals("gif") ||
                            extension.equals("jpeg") ||
                            extension.equals("jpg") ||
                            extension.equals("png"))) {
                                return false;
                        }
                    }
                }
            }
        }catch(NullPointerException ex){
            return false;
        }
        
        return true;
    }
  

    public static String getExtension(File f){
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}

