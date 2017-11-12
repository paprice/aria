/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author despa
 */
public class LocalUser {
 
    private int nbUser;
    private List<String> UserNames;
    
    
    public LocalUser(){
        nbUser = 0;
        UserNames = new ArrayList<>();
    }
    
    public boolean CreateNewUser(String user){
        if(!UserNames.contains(user)){
            UserNames.add(user);
            return true;
        }
        return false;
    }
    
    
    
}
