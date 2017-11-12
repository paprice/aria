/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import Files.LoadJson;
import Files.WriteJson;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author despa
 */
public class LocalUser {
 
    private int nbUser;
    private List<String> UserNames;
    
    
    public LocalUser(){
        nbUser = 0;
        try {
            UserNames = LoadJson.GetUsers();
        } catch (IOException ex) {
            Logger.getLogger(LocalUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
    public boolean CreateNewUser(String user){
        if(!UserNames.contains(user)){
            nbUser++;
            UserNames.add(user);
            WriteJson.WriteData(nbUser, UserNames);
            return true;
        }
        return false;
    }
    
    
    
}
