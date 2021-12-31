/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

/**
 *
 * @author benza
 */
public class KeyValue {
    public int key;
    public Object obj;
    public KeyValue(int key, Object obj){
        this.key=key;
        this.obj=obj;
    }
    @Override
    public String toString(){
        return (String)this.obj;
    }
}
